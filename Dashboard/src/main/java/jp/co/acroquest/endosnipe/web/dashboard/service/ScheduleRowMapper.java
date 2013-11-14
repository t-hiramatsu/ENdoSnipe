package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SchedulingReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.ReportDefinition;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SchedulingReportDefinition;
import jp.co.acroquest.endosnipe.web.dashboard.util.ReportUtil;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Getting scheduling data 
 * @author khinlay
 *
 */
public class ScheduleRowMapper implements RowMapper<SchedulingReportDefinitionDto>
{
    /**
     * time format for hour and minute.
     */
    private static final String TIME_FORMAT = "HH:mm";

    /**
     * date format
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** ロガー。 */
    private static final ENdoSnipeLogger LOGGER = ENdoSnipeLogger.getLogger(MapService.class);

    /** connect with database */
    String[] springConfig = { "spring/batch/jobs/job-extract-users.xml" };

    /** create class path */
    ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);

    /** create datasource */
    DataSource source = (DataSource)context.getBean("dataSource");

    /** create template */
    JdbcTemplate jTemplate = new JdbcTemplate(source);

    /**
     * used for day
     */
    private final Map<String, Integer> dayMap_ = new HashMap<String, Integer>();

    public ScheduleRowMapper()
    {
        this.dayMap_.put("Sunday", Calendar.SUNDAY);
        this.dayMap_.put("Monday", Calendar.MONDAY);
        this.dayMap_.put("Tuesday", Calendar.TUESDAY);
        this.dayMap_.put("Wednesday", Calendar.WEDNESDAY);
        this.dayMap_.put("Thursday", Calendar.THURSDAY);
        this.dayMap_.put("Friday", Calendar.FRIDAY);
        this.dayMap_.put("Saturday", Calendar.SATURDAY);
    }

    /**
     * get scheduling data
     * @param rs got scheduling data
     * @param rowNum row number
     * @return null
     */
    public SchedulingReportDefinitionDto mapRow(final ResultSet rs, final int rowNum)
    {

        this.createReport(rs);
        return null;
    }

    /**
     * create report.
     * @param rs scheduling data
     */
    private void createReport(final ResultSet rs)
    {
        List<SchedulingReportDefinitionDto> schedulingReportDefinitionDtos =
                new ArrayList<SchedulingReportDefinitionDto>();
        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                new SchedulingReportDefinitionDto();
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

        Calendar time = Calendar.getInstance();
        try
        {
            schedulingReportDefinitionDto.setReportId(rs.getInt("report_id"));

            schedulingReportDefinitionDto.setReportName(rs.getString("report_name"));

            schedulingReportDefinitionDto.setTargetMeasurementName(rs.getString("target_measurement_name"));

            schedulingReportDefinitionDto.setTerm(rs.getString("schedule_term"));
            try
            {
                time.setTime(timeFormat.parse(rs.getString("schedule_time")));
            }
            catch (ParseException ex)
            {
                ex.printStackTrace();
            }
            schedulingReportDefinitionDto.setTime(time);
            schedulingReportDefinitionDto.setLastExportedTime(rs.getTimestamp("last_export_report_time"));
            schedulingReportDefinitionDto.setDay(rs.getString("schedule_day"));
            schedulingReportDefinitionDto.setDate(rs.getString("schedule_date"));
        }
        catch (SQLException ex1)
        {
            ex1.printStackTrace();
        }

        String reportName = "";
        ReportDefinition definition = this.convertScheduleToReport(schedulingReportDefinitionDto);
        definition.status_ = "creating";

        ReportDefinitionDto definitionDto = ReportUtil.convertReportDifinitionDto(definition);
        //put the report to the queue for export
        ReportUtil.createReport(definitionDto);
        String sql =

                "insert into REPORT_EXPORT_RESULT (REPORT_NAME,TARGET_MEASUREMENT_NAME,FM_TIME,TO_TIME, STATUS)"

                + " values (" + "'" + definition.reportName_ + "'," + "'"
                        + definition.targetMeasurementName_ + "', " + "'" + definition.fmTime_
                        + "', " + "'" + definition.toTime_ + "'," + "'" + definition.status_ + "')";

        jTemplate.execute(sql);

        reportName = definitionDto.getReportName();

        SchedulingReportDefinition schedulingReportDefinition = new SchedulingReportDefinition();

        DateFormat scheduleTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        Calendar scheduleTime = Calendar.getInstance();

        try
        {
            scheduleTime.setTime(scheduleTimeFormat.parse(rs.getString("schedule_time")));
            schedulingReportDefinition.time_ = timeFormat.format(time.getTime());

            Calendar lastExportedCalendar = Calendar.getInstance();
            Calendar currentTimeCalendar = Calendar.getInstance();

            lastExportedCalendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            lastExportedCalendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            if (rs.getString("schedule_time") != null && rs.getString("schedule_day") == null
                    && rs.getString("schedule_date") == null)
            {
                long lastExportedLong = lastExportedCalendar.getTimeInMillis();
                long currentTimeLong = currentTimeCalendar.getTimeInMillis();

                if (lastExportedLong <= currentTimeLong)
                {
                    lastExportedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            if (rs.getString("schedule_day") != null)
            {
                dayMap_.get(rs.getString("schedule_day"));
                int lastExportedDay =
                        (dayMap_.get(rs.getString("schedule_day")))
                                - lastExportedCalendar.get(Calendar.DAY_OF_WEEK);
                lastExportedCalendar.add(Calendar.DAY_OF_MONTH, lastExportedDay);
                long lastExportedLong = lastExportedCalendar.getTimeInMillis();
                long currentTimeLong = currentTimeCalendar.getTimeInMillis();

                if (lastExportedLong <= currentTimeLong)
                {
                    lastExportedCalendar.add(Calendar.DAY_OF_MONTH, 7);
                }
            }

            int date = 0;
            if (rs.getString("schedule_date") != null)
            {
                date = Integer.parseInt(rs.getString("schedule_date"));
                lastExportedCalendar.set(Calendar.DAY_OF_MONTH, date);
                long lastExportedLong = lastExportedCalendar.getTimeInMillis();
                long currentTimeLong = currentTimeCalendar.getTimeInMillis();

                if (lastExportedLong <= currentTimeLong)
                {
                    lastExportedCalendar.add(Calendar.MONTH, 1);
                }
            }

            if (date > lastExportedCalendar.get(Calendar.DAY_OF_MONTH)
                    && (lastExportedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) >= date)

            {

                lastExportedCalendar.set(Calendar.DAY_OF_MONTH, date);

            }
            Timestamp lastExportedTime = new Timestamp(lastExportedCalendar.getTimeInMillis());
            schedulingReportDefinition.lastExportedTime_ = lastExportedTime;

            jTemplate.batchUpdate(new String[] { "update SCHEDULING_REPORT_DEFINITION set LAST_EXPORT_REPORT_TIME = '"
                    + lastExportedTime + "' where REPORT_NAME =" + "'" + reportName + "'" });

        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * convert to report definition.
     * @param scheduling scheduling report dto
     * @return definiton
     */
    private ReportDefinition convertScheduleToReport(final SchedulingReportDefinitionDto scheduling)
    {
        ReportDefinition definition = new ReportDefinition();
        definition.reportId_ = scheduling.getReportId();
        definition.reportName_ = scheduling.getReportName();
        definition.targetMeasurementName_ = scheduling.getTargetMeasurementName();

        Timestamp lastExportedTime = scheduling.getLastExportedTime();
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTimeInMillis(lastExportedTime.getTime());

        if ("DAILY".equals(scheduling.getTerm()))
        {
            fromCalendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        else if ("WEEKLY".equals(scheduling.getTerm()))
        {
            fromCalendar.add(Calendar.DAY_OF_MONTH, -7);
        }
        else
        {
            fromCalendar.add(Calendar.MONTH, -1);
        }

        Date date = new Date(lastExportedTime.getTime());
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        definition.toTime_ = format.format(date);
        definition.fmTime_ = format.format(fromCalendar.getTime());

        return definition;

    }

    /**
      * ReportDefinitionオブジェクトをReportDefinitionDtoオブジェクトに変換する。
      * 
      * @param reportDefinition
      *            ReportDefinitionオブジェクト
      * @return ReportDefinitionDtoオブジェクト
      */
    private ReportDefinitionDto convertReportDifinitionDto(final ReportDefinition reportDefinition)
    {

        ReportDefinitionDto definitionDto = new ReportDefinitionDto();
        definitionDto.setReportId(reportDefinition.reportId_);
        definitionDto.setReportName(reportDefinition.reportName_);
        definitionDto.setTargetMeasurementName(reportDefinition.targetMeasurementName_);

        return definitionDto;
    }
}
package jp.co.acroquest.endosnipe.web.explorer.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import jp.co.acroquest.endosnipe.web.explorer.dto.ReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.dto.SchedulingReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.explorer.entity.ReportDefinition;
import jp.co.acroquest.endosnipe.web.explorer.entity.SchedulingReportDefinition;
import jp.co.acroquest.endosnipe.web.explorer.util.ReportUtil;

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
    /**constants integer value is 7*/
    private static final int NUMBER_SEVEN = 7;

    /**
     * time format for hour and minute.
     */
    private static final String TIME_FORMAT = "HH:mm";

    /**
     * date format
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** connect with database */
    String[] springConfig_ = { "spring/batch/jobs/job-extract-users.xml" };

    /** create class path */
    ApplicationContext context_ = new ClassPathXmlApplicationContext(springConfig_);

    /** create datasource */
    DataSource source_ = (DataSource)context_.getBean("dataSource");

    /** create template */
    JdbcTemplate jTemplate_ = new JdbcTemplate(source_);

    /**
     * used for day
     */
    private final Map<String, Integer> dayMap_ = new HashMap<String, Integer>();

    /**
     * used for day
     */
    private static final int DAY_OF_WEEK = -7;

    /**
     * Change int day to String day
     */
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
        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                new SchedulingReportDefinitionDto();
        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
        Calendar time = Calendar.getInstance();
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
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
            schedulingReportDefinitionDto.setPlanExportedTime(rs.getTimestamp("plan_export_report_time"));
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

        //check report is already exported or not
        int countOfReport =
                this.jTemplate_.queryForInt("select count(*) from REPORT_EXPORT_RESULT where REPORT_NAME = ?",
                                            definition.reportName_);
        //if report exported is first time, send to the client side and add to tree 
        //for reappear even user deleted the previous exported report on tree.
        if (countOfReport < 1)
        {
            ReportUtil.sendSchedulingReportDefinition(schedulingReportDefinitionDto, "add");
        }

        String sql =
                "insert into REPORT_EXPORT_RESULT (REPORT_NAME,TARGET_MEASUREMENT_NAME,FM_TIME,TO_TIME, STATUS)"

                + " values (" + "'" + definition.reportName_ + "'," + "'"
                        + definition.targetMeasurementName_ + "', " + "'" + definition.fmTime_
                        + "', " + "'" + definition.toTime_ + "'," + "'" + definition.status_ + "')";
        jTemplate_.execute(sql);

        reportName = definitionDto.getReportName();
        SchedulingReportDefinition schedulingReportDefinition = new SchedulingReportDefinition();
        DateFormat scheduleTimeFormat = new SimpleDateFormat(TIME_FORMAT);
        Calendar scheduleTime = Calendar.getInstance();
        scheduleTime.set(Calendar.SECOND, 0);
        scheduleTime.set(Calendar.MILLISECOND, 0);
        try
        {
            scheduleTime.setTime(scheduleTimeFormat.parse(rs.getString("schedule_time")));
            schedulingReportDefinition.time_ = timeFormat.format(time.getTime());
            Calendar planExportedCalendar = Calendar.getInstance();
            planExportedCalendar.set(Calendar.SECOND, 0);
            planExportedCalendar.set(Calendar.MILLISECOND, 0);
            Calendar currentTimeCalendar = Calendar.getInstance();
            currentTimeCalendar.set(Calendar.SECOND, 0);
            currentTimeCalendar.set(Calendar.MILLISECOND, 0);
            planExportedCalendar.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
            planExportedCalendar.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
            if (rs.getString("schedule_time") != null && rs.getString("schedule_day") == null
                    && rs.getString("schedule_date") == null)
            {
                long planExportedLong = planExportedCalendar.getTimeInMillis();
                long currentTimeLong = currentTimeCalendar.getTimeInMillis();
                if (planExportedLong <= currentTimeLong)
                {
                    planExportedCalendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
            if (rs.getString("schedule_day") != null)
            {
                dayMap_.get(rs.getString("schedule_day"));
                int planExportedDay =
                        (dayMap_.get(rs.getString("schedule_day")))
                                - planExportedCalendar.get(Calendar.DAY_OF_WEEK);
                planExportedCalendar.add(Calendar.DAY_OF_MONTH, planExportedDay);
                long planExportedLong = planExportedCalendar.getTimeInMillis();
                long currentTimeLong = currentTimeCalendar.getTimeInMillis();

                if (planExportedLong <= currentTimeLong)
                {
                    planExportedCalendar.add(Calendar.DAY_OF_MONTH, NUMBER_SEVEN);
                }
            }
            int date = 0;
            if (rs.getString("schedule_date") != null)
            {
                date = Integer.parseInt(rs.getString("schedule_date"));
                planExportedCalendar.set(Calendar.DAY_OF_MONTH, date);
                long planExportedLong = planExportedCalendar.getTimeInMillis();
                long currentTimeLong = currentTimeCalendar.getTimeInMillis();

                if (planExportedLong <= currentTimeLong)
                {
                    planExportedCalendar.add(Calendar.MONTH, 1);
                }
            }
            if (date > planExportedCalendar.get(Calendar.DAY_OF_MONTH)
                    && (planExportedCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)) >= date)
            {
                planExportedCalendar.set(Calendar.DAY_OF_MONTH, date);
            }
            Timestamp planExportedTime = new Timestamp(planExportedCalendar.getTimeInMillis());
            schedulingReportDefinition.planExportedTime_ = planExportedTime;
            jTemplate_.batchUpdate(new String[] { "update SCHEDULING_REPORT_DEFINITION set PLAN_EXPORT_REPORT_TIME = '"
                    + planExportedTime + "' where REPORT_NAME =" + "'" + reportName + "'" });
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

        Timestamp planExportedTime = scheduling.getPlanExportedTime();
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);
        fromCalendar.setTimeInMillis(planExportedTime.getTime());

        if ("DAILY".equals(scheduling.getTerm()))
        {
            fromCalendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        else if ("WEEKLY".equals(scheduling.getTerm()))
        {
            fromCalendar.add(Calendar.DAY_OF_MONTH, DAY_OF_WEEK);
        }
        else
        {
            fromCalendar.add(Calendar.MONTH, -1);
        }

        Date date = new Date(planExportedTime.getTime());
        DateFormat format = new SimpleDateFormat(DATE_FORMAT);
        definition.toTime_ = format.format(date);
        definition.fmTime_ = format.format(fromCalendar.getTime());

        return definition;

    }
}
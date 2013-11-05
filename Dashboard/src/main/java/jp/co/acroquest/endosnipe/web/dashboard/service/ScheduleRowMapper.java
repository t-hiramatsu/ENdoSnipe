package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import jp.co.acroquest.endosnipe.common.logger.ENdoSnipeLogger;
import jp.co.acroquest.endosnipe.web.dashboard.dto.ReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.dto.SchedulingReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.ReportDefinition;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SchedulingReportDefinition;

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
        throws SQLException
    {

        try
        {
            /* System.out.println(rs.getString("report_name"));*/
            Calendar calendar = Calendar.getInstance();
            System.out.println("Current day:" + calendar.get(Calendar.DAY_OF_MONTH));
            System.out.println("last date:"
                    + Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
            if ("DAILY".equals(rs.getString("schedule_term")))
            {
                this.createDaily(rs);
            }
            if ("WEEKLY".equals(rs.getString("schedule_term")))
            {
                this.createWeekly(rs);
            }
            if ("MONTHLY".equals(rs.getString("schedule_term")))
            {
                this.createMonthly(rs);
            }

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * create daily.
     * @param rs scheduling data
     */
    private void createDaily(final ResultSet rs)
    {
        Calendar time = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try
        {
            time.setTime(timeFormat.parse(rs.getString("schedule_time")));
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        if (time.get(Calendar.HOUR_OF_DAY) == currentTime.get(Calendar.HOUR_OF_DAY)
                && time.get(Calendar.MINUTE) == currentTime.get(Calendar.MINUTE))
        {
            this.createReport(rs);
        }
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
        try
        {
            schedulingReportDefinitionDto.setReportId(rs.getInt("report_id"));
        }
        catch (SQLException ex1)
        {
            ex1.printStackTrace();
        }
        try
        {
            schedulingReportDefinitionDto.setReportName(rs.getString("report_name"));
        }
        catch (SQLException ex1)
        {
            ex1.printStackTrace();
        }
        try
        {
            schedulingReportDefinitionDto.setTargetMeasurementName(rs.getString("target_measurement_name"));
        }
        catch (SQLException ex1)
        {
            ex1.printStackTrace();
        }
        try
        {
            schedulingReportDefinitionDto.setTerm(rs.getString("schedule_term"));
        }
        catch (SQLException ex1)
        {
            ex1.printStackTrace();
        }

        DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

        Calendar time = Calendar.getInstance();
        try
        {
            try
            {
                time.setTime(timeFormat.parse(rs.getString("schedule_time")));
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        }
        catch (ParseException ex)
        {
            ex.printStackTrace();
        }
        schedulingReportDefinitionDto.setTime(time);
        try
        {
            schedulingReportDefinitionDto.setLastExportedTime(rs.getTimestamp("last_export_report_time"));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        try
        {
            schedulingReportDefinitionDto.setDay(rs.getString("schedule_day"));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        try
        {
            schedulingReportDefinitionDto.setDate(rs.getString("schedule_date"));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        schedulingReportDefinitionDtos.add(schedulingReportDefinitionDto);
        String reportName = "";
        for (int index = 0; index < schedulingReportDefinitionDtos.size(); index++)
        {
            SchedulingReportDefinitionDto scheduling = schedulingReportDefinitionDtos.get(index);
            ReportDefinition definition = this.convertScheduleToReport(scheduling);
            ReportDefinitionDto definitionDto = this.convertReportDifinitionDto(definition);
            reportName = definitionDto.getReportName();
        }
        //export report(upper)
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

            if (rs.getString("schedule_date") != null)
            {
                int date = Integer.parseInt(rs.getString("schedule_date"));
                lastExportedCalendar.set(Calendar.DAY_OF_MONTH, date);
                long lastExportedLong = lastExportedCalendar.getTimeInMillis();
                long currentTimeLong = currentTimeCalendar.getTimeInMillis();

                if (lastExportedLong <= currentTimeLong)
                {
                    lastExportedCalendar.add(Calendar.MONTH, 1);
                }
            }
            Timestamp lastExportedTime = new Timestamp(lastExportedCalendar.getTimeInMillis());
            schedulingReportDefinition.lastExportedTime_ = lastExportedTime;

            String[] springConfig = { "spring/batch/jobs/job-extract-users.xml" };

            ApplicationContext context = new ClassPathXmlApplicationContext(springConfig);
            DataSource source = (DataSource)context.getBean("dataSource");
            JdbcTemplate jt = new JdbcTemplate(source);
            jt.batchUpdate(new String[] { "update SCHEDULING_REPORT_DEFINITION set LAST_EXPORT_REPORT_TIME = '"
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

    private void createWeekly(final ResultSet rs)
    {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay = "";
        if (Calendar.MONDAY == dayOfWeek)
        {
            weekDay = "Monday";
        }
        else if (Calendar.TUESDAY == dayOfWeek)
        {
            weekDay = "Tuesday";
        }
        else if (Calendar.WEDNESDAY == dayOfWeek)
        {
            weekDay = "Wednesday";
        }
        else if (Calendar.THURSDAY == dayOfWeek)
        {
            weekDay = "Thursday";
        }
        else if (Calendar.FRIDAY == dayOfWeek)
        {
            weekDay = "Friday";
        }
        else if (Calendar.SATURDAY == dayOfWeek)
        {
            weekDay = "Saturday";
        }
        else if (Calendar.SUNDAY == dayOfWeek)
        {
            weekDay = "Sunday";
        }
        try
        {
            if (weekDay.equals(rs.getString("schedule_day")))
            {
                this.createDaily(rs);
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * create monthly data.
     * @param rs scheduling data
     */
    private void createMonthly(final ResultSet rs)
    {
        Calendar calendar = Calendar.getInstance();
        int scheduleDate = 0;
        try
        {
            scheduleDate = Integer.parseInt(rs.getString("schedule_date"));
        }
        catch (NumberFormatException ex)
        {
            ex.printStackTrace();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }

        if (calendar.get(Calendar.DAY_OF_MONTH) == scheduleDate)
        {
            this.createDaily(rs);
        }
        else if (calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH))
        {
            this.createDaily(rs);
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
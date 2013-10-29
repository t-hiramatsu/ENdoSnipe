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

import jp.co.acroquest.endosnipe.web.dashboard.dto.SchedulingReportDefinitionDto;
import jp.co.acroquest.endosnipe.web.dashboard.entity.SchedulingReportDefinition;

import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * @author khinlay
 *
 */
public class ScheduleRowMapper implements RowMapper<List<SchedulingReportDefinitionDto>>
{
    /**
     * Format calendar for hour and minute
     */
    private static final String TIME_FORMAT = "HH:mm";

    private final Map<String, Integer> dayMap_ = new HashMap<String, Integer>();

    /**
     * default constructor
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
     * Get data by using spring batch
     */
    public List<SchedulingReportDefinitionDto> mapRow(final ResultSet rs, final int rowNum)
    {
        List<SchedulingReportDefinitionDto> schedulingReportDefinitionDtos =
                new ArrayList<SchedulingReportDefinitionDto>();
        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                new SchedulingReportDefinitionDto();
        try
        {
            while (rs.next())
            {
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
                SchedulingReportDefinition schedulingReportDefinition =
                        new SchedulingReportDefinition();
                /*schedulingReportDefinition.reportId_ = Integer.parseInt(rs.getString("report_id"));
                schedulingReportDefinition.reportName_ = rs.getString("report_name");
                schedulingReportDefinition.targetMeasurementName_ =
                        rs.getString("target_measurement_name");
                schedulingReportDefinition.term_ = rs.getString("schedule_term");
                schedulingReportDefinition.day_ = rs.getString("schedule_day");*/

                schedulingReportDefinition.reportId_ = Integer.parseInt(rs.getString("report_id"));
                DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);
                Calendar time = Calendar.getInstance();
                try
                {
                    time.setTime(timeFormat.parse(rs.getString("schedule_time")));
                }
                catch (ParseException ex)
                {
                    ex.printStackTrace();
                }

                schedulingReportDefinition.time_ = timeFormat.format(time.getTime());

                /*schedulingReportDefinition.date_ = rs.getString("schedule_date");*/

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
                    /*lastExportedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);*/
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
                    /*lastExportedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);*/
                }
                Timestamp lastExportedTime = new Timestamp(lastExportedCalendar.getTimeInMillis());
                schedulingReportDefinition.lastExportedTime_ = lastExportedTime;
                rs.getString("last_export_report_time");

            }

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        return null;
        // return schedulingReportDefinitionDtos;
    }

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
        System.out.println("hour:" + time.get(Calendar.HOUR_OF_DAY));
        System.out.println("minute:" + time.get(Calendar.MINUTE));
        System.out.println("current hour:" + currentTime.get(Calendar.HOUR_OF_DAY));
        System.out.println("current minute:" + currentTime.get(Calendar.MINUTE));
        if (time.get(Calendar.HOUR_OF_DAY) == currentTime.get(Calendar.HOUR_OF_DAY)
                && time.get(Calendar.MINUTE) == currentTime.get(Calendar.MINUTE))
        {
            this.createReport(rs);
        }
    }

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

        System.out.println("reportId:" + schedulingReportDefinitionDto.getReportId());
        System.out.println("reportName:" + schedulingReportDefinitionDto.getReportName());
        System.out.println("TargetMeasurementName:"
                + schedulingReportDefinitionDto.getTargetMeasurementName());
        System.out.println("Term:" + schedulingReportDefinitionDto.getTerm());
        System.out.println("Time:" + schedulingReportDefinitionDto.getTime());
        System.out.println("Day:" + schedulingReportDefinitionDto.getDay());
        System.out.println("Date:" + schedulingReportDefinitionDto.getDate());
        System.out.println("Last Export Time:"
                + schedulingReportDefinitionDto.getLastExportedTime());

        schedulingReportDefinitionDtos.add(schedulingReportDefinitionDto);

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
    }
}
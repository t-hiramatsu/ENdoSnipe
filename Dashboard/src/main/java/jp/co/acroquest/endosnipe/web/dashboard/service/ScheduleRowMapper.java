package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.co.acroquest.endosnipe.web.dashboard.dto.SchedulingReportDefinitionDto;

import org.springframework.jdbc.core.RowMapper;

public class ScheduleRowMapper implements RowMapper<List<SchedulingReportDefinitionDto>>
{
    private static final String TIME_FORMAT = "HH:mm";

    public ScheduleRowMapper()
    {

    }

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
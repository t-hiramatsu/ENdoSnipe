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

    public List<SchedulingReportDefinitionDto> mapRow(final ResultSet rs, final int rowNum)
        throws SQLException
    {
        List<SchedulingReportDefinitionDto> schedulingReportDefinitionDtos =
                new ArrayList<SchedulingReportDefinitionDto>();
        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                new SchedulingReportDefinitionDto();
        while (rs.next())
        {
            schedulingReportDefinitionDto.setReportId(rs.getInt("report_id"));
            schedulingReportDefinitionDto.setReportName(rs.getString("report_name"));
            schedulingReportDefinitionDto.setTargetMeasurementName(rs.getString("target_measurement_name"));
            schedulingReportDefinitionDto.setTerm(rs.getString("schedule_term"));

            DateFormat timeFormat = new SimpleDateFormat(TIME_FORMAT);

            Calendar time = Calendar.getInstance();
            try
            {
                time.setTime(timeFormat.parse(rs.getString("schedule_time")));
            }
            catch (ParseException ex)
            {
                // TODO 自動生成された catch ブロック
                ex.printStackTrace();
            }
            schedulingReportDefinitionDto.setTime(time);
            schedulingReportDefinitionDto.setLastExportedTime(rs.getTimestamp("last_export_report_time"));

            schedulingReportDefinitionDto.setDay(rs.getString("schedule_day"));
            schedulingReportDefinitionDto.setDate(rs.getString("schedule_date"));

            System.out.println("value of reportId:" + schedulingReportDefinitionDto.getReportId());
            System.out.println("value of reportName:"
                    + schedulingReportDefinitionDto.getReportName());
            System.out.println("value of TargetMeasurementName:"
                    + schedulingReportDefinitionDto.getTargetMeasurementName());
            System.out.println("value of Term:" + schedulingReportDefinitionDto.getTerm());
            System.out.println("value of Time:" + schedulingReportDefinitionDto.getTime());
            System.out.println("value of Day:" + schedulingReportDefinitionDto.getDay());
            System.out.println("value of Date:" + schedulingReportDefinitionDto.getDate());
            System.out.println("value of Last Export Time:"
                    + schedulingReportDefinitionDto.getLastExportedTime());

            schedulingReportDefinitionDtos.add(schedulingReportDefinitionDto);
        }

        return schedulingReportDefinitionDtos;
    }
}
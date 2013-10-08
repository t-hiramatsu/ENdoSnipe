package jp.co.acroquest.endosnipe.web.dashboard.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import jp.co.acroquest.endosnipe.web.dashboard.dto.SchedulingReportDefinitionDto;

import org.springframework.jdbc.core.RowMapper;

public class ScheduleRowMapper implements RowMapper<SchedulingReportDefinitionDto>
{

    public SchedulingReportDefinitionDto mapRow(final ResultSet rs, final int rowNum)
        throws SQLException
    {

        SchedulingReportDefinitionDto schedulingReportDefinitionDto =
                new SchedulingReportDefinitionDto();

        schedulingReportDefinitionDto.setReportId(rs.getInt("report_id"));
        schedulingReportDefinitionDto.setReportName(rs.getString("report_name"));
        schedulingReportDefinitionDto.setTargetMeasurementName(rs.getString("target_measurement_name"));
        schedulingReportDefinitionDto.setTerm(rs.getString("schedule_term"));
        /*schedulingReportDefinitionDto.setTime(rs.getDate("schedule_time"));*/
        schedulingReportDefinitionDto.setLastExportedTime(rs.getTimestamp("last_export_report_time"));

        /*Calendar cal = new GregorianCalendar();
        cal.setTime(rs.getTime("schedule_time"));
        schedulingReportDefinitionDto.setTime(cal);*/

        schedulingReportDefinitionDto.setDay(rs.getString("schedule_day"));
        schedulingReportDefinitionDto.setDate(rs.getString("schedule_date"));
        System.out.println("value of reportId:" + schedulingReportDefinitionDto.getReportId());
        System.out.println("value of reportName:" + schedulingReportDefinitionDto.getReportName());
        System.out.println("value of TargetMeasurementName:"
                + schedulingReportDefinitionDto.getTargetMeasurementName());
        System.out.println("value of Term:" + schedulingReportDefinitionDto.getTerm());
        System.out.println("value of Day:" + schedulingReportDefinitionDto.getDay());
        System.out.println("value of Date:" + schedulingReportDefinitionDto.getDate());
        System.out.println("value of Last Export Time:"
                + schedulingReportDefinitionDto.getLastExportedTime());

        return schedulingReportDefinitionDto;
    }
}
package com.example.etl.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class KpiRepository {

    private final EntityManager entityManager;

    public boolean kpisExistForCurrentDate() {
        String query = "SELECT COUNT(*) > 0 " +
                "FROM public.kpis " +
                "WHERE kpi_date = CURRENT_DATE;";

        return (boolean) entityManager.createNativeQuery(query)
                .getSingleResult();
    }

    public void insertKpis() {
        String query = "INSERT INTO kpis (kpi_name, kpi_date, kpi_value) " +
                "SELECT 'mean_break_length_in_minutes', CURRENT_TIMESTAMP, EXTRACT(EPOCH FROM AVG(breaks.finish - breaks.start))/60 " +
                "FROM public.breaks;";

        query += "INSERT INTO kpis (kpi_name, kpi_date, kpi_value) " +
                "SELECT 'mean_shift_cost', CURRENT_TIMESTAMP, AVG(cost) " +
                "FROM shifts;";

        query += "INSERT INTO kpis (kpi_name, kpi_date, kpi_value) " +
                "SELECT 'max_allowance_cost_14d', CURRENT_TIMESTAMP, MAX(public.allowances.cost) " +
                "FROM allowances " +
                "WHERE shift_id IN (SELECT id " +
                "FROM shifts " +
                "WHERE date >= CURRENT_DATE - INTERVAL '14 days' AND date <= CURRENT_DATE);";

        query += "INSERT INTO kpis (kpi_name, kpi_date, kpi_value) " +
                "SELECT 'max_break_free_shift_period_in_days', CURRENT_TIMESTAMP, COUNT(*) AS break_free_shift_period_in_days " +
                "FROM (" +
                "   SELECT shifts_without_breaks.*, row_number() over (order by date) as seqnum " +
                "      FROM (" +
                "       SELECT * " +
                "       FROM public.shifts " +
                "       WHERE id NOT IN (SELECT shift_id FROM public.breaks)" +
                "      ) shifts_without_breaks" +
                ") shifts_without_breaks " +
                "GROUP BY date - seqnum * INTERVAL '1 days' " +
                "ORDER BY break_free_shift_period_in_days DESC " +
                "LIMIT 1;";

        query += "INSERT INTO kpis (kpi_name, kpi_date, kpi_value) " +
                "SELECT 'min_shift_length_in_hours', CURRENT_TIMESTAMP, EXTRACT(EPOCH FROM MIN(shifts.finish - shifts.start))/3600 " +
                "FROM shifts;";

        query += "INSERT INTO kpis (kpi_name, kpi_date, kpi_value) " +
                "SELECT 'total_number_of_paid_breaks', CURRENT_TIMESTAMP, COUNT(*) " +
                "FROM breaks " +
                "WHERE paid = true;";

        entityManager.createNativeQuery(query)
                .executeUpdate();
    }

}

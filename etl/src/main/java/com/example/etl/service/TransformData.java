package com.example.etl.service;

import com.example.etl.client.response.AllowanceResponse;
import com.example.etl.client.response.AwardInterpretationResponse;
import com.example.etl.client.response.BreakResponse;
import com.example.etl.client.response.ShiftResponse;
import com.example.etl.client.response.ShiftsApiResponse;
import com.example.etl.model.Allowance;
import com.example.etl.model.AwardInterpretation;
import com.example.etl.model.Break;
import com.example.etl.model.Shift;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.example.etl.util.TransformationUtil.transformList;

@Service
public class TransformData {

    public List<Shift> execute(final ShiftsApiResponse response) {
        return response.getResults().stream()
                .map(this::toShift)
                .toList();
    }

    private Shift toShift(final ShiftResponse shiftResponse) {
        final Shift shift = Shift.builder()
                .id(shiftResponse.getId())
                .date(shiftResponse.getDate())
                .start(toDate(shiftResponse.getStart()))
                .finish(toDate(shiftResponse.getFinish()))
                .build();

        shift.setBreaks(
                transformList(shiftResponse.getBreaks(), breakResponse -> toBreak(breakResponse, shift))
        );
        shift.setAllowances(
                transformList(shiftResponse.getAllowances(), allowanceResponse -> toAllowance(allowanceResponse, shift))
        );
        shift.setAwardInterpretations(
                transformList(shiftResponse.getAwardInterpretations(), awardInterpretationResponse -> toAwardInterpretation(awardInterpretationResponse, shift))
        );
        shift.setCost(calculateShiftCost(shift));
        
        return shift;
    }

    private Date toDate(final long milliseconds) {
        return Date.from(Instant.ofEpochMilli(milliseconds));
    }

    private Break toBreak(final BreakResponse breakResponse, final Shift shift) {
        return Break.builder()
                .id(breakResponse.getId())
                .start(toDate(breakResponse.getStart()))
                .finish(toDate(breakResponse.getFinish()))
                .paid(breakResponse.isPaid())
                .shift(shift)
                .build();
    }

    private Allowance toAllowance(final AllowanceResponse allowanceResponse, final Shift shift) {
        return Allowance.builder()
                .id(allowanceResponse.getId())
                .value(allowanceResponse.getValue())
                .cost(allowanceResponse.getCost())
                .shift(shift)
                .build();
    }

    private AwardInterpretation toAwardInterpretation(final AwardInterpretationResponse awardInterpretationResponse, final Shift shift) {
        return AwardInterpretation.builder()
                .id(awardInterpretationResponse.getId())
                .date(awardInterpretationResponse.getDate())
                .units(awardInterpretationResponse.getUnits())
                .cost(awardInterpretationResponse.getCost())
                .shift(shift)
                .build();
    }

    private double calculateShiftCost(final Shift shift) {
        final double allowancesCost = shift.getAllowances().stream()
                .mapToDouble(Allowance::getCost)
                .sum();
        final double awardInterpretationsCost = shift.getAwardInterpretations().stream()
                .mapToDouble(AwardInterpretation::getCost)
                .sum();

        return allowancesCost + awardInterpretationsCost;
    }
}

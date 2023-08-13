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
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class TransformData {

    public List<Shift> execute(ShiftsApiResponse response) {
        System.out.println(response.getResults().get(0).getId());
        return response.getResults().stream()
                .map(this::toShift)
                .toList();
    }

    private Shift toShift(ShiftResponse shiftResponse) {
        Shift shift = Shift.builder()
                .id(shiftResponse.getId())
                .date(shiftResponse.getDate())
                .start(toDate(shiftResponse.getStart()))
                .finish(toDate(shiftResponse.getFinish()))
                .build();

        shift.setBreaks(toBreaks(shiftResponse.getBreaks(), shift));
        shift.setAllowances(toAllowances(shiftResponse.getAllowances(), shift));
        shift.setAwardInterpretations(toAwardInterpretations(shiftResponse.getAwardInterpretations(), shift));
        shift.setCost(calculateShiftCost(shift));
        
        return shift;
    }

    private Date toDate(long milliseconds) {
        return Date.from(Instant.ofEpochMilli(milliseconds));
    }

    private List<Break> toBreaks(List<BreakResponse> breaksResponse, Shift shift) {
        if (breaksResponse == null) {
            return Collections.emptyList();
        }

        return breaksResponse.stream()
                .map(breakResponse -> toBreak(breakResponse, shift))
                .toList();
    }

    private Break toBreak(BreakResponse breakResponse, Shift shift) {
        return Break.builder()
                .id(breakResponse.getId())
                .start(toDate(breakResponse.getStart()))
                .finish(toDate(breakResponse.getFinish()))
                .paid(breakResponse.isPaid())
                .shift(shift)
                .build();
    }

    private List<Allowance> toAllowances(List<AllowanceResponse> allowancesResponse, Shift shift) {
        if (allowancesResponse == null) {
            return Collections.emptyList();
        }

        return allowancesResponse.stream()
                .map(allowanceResponse -> toAllowance(allowanceResponse, shift))
                .toList();
    }

    private Allowance toAllowance(AllowanceResponse allowanceResponse, Shift shift) {
        return Allowance.builder()
                .id(allowanceResponse.getId())
                .value(allowanceResponse.getValue())
                .cost(allowanceResponse.getCost())
                .shift(shift)
                .build();
    }

    private List<AwardInterpretation> toAwardInterpretations(List<AwardInterpretationResponse> awardInterpretationsResponse, Shift shift) {
        if (awardInterpretationsResponse == null) {
            return Collections.emptyList();
        }

        return awardInterpretationsResponse.stream()
                .map(awardInterpretationResponse -> toAwardInterpretation(awardInterpretationResponse, shift))
                .toList();
    }

    private AwardInterpretation toAwardInterpretation(AwardInterpretationResponse awardInterpretationResponse, Shift shift) {
        return AwardInterpretation.builder()
                .id(awardInterpretationResponse.getId())
                .date(awardInterpretationResponse.getDate())
                .units(awardInterpretationResponse.getUnits())
                .cost(awardInterpretationResponse.getCost())
                .shift(shift)
                .build();
    }

    private double calculateShiftCost(Shift shift) {
        final double allowancesCost = shift.getAllowances().stream()
                .mapToDouble(Allowance::getCost)
                .sum();
        final double awardInterpretationsCost = shift.getAwardInterpretations().stream()
                .mapToDouble(AwardInterpretation::getCost)
                .sum();

        return allowancesCost + awardInterpretationsCost;
    }
}

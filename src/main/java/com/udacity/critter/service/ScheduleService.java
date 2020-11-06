package com.udacity.critter.service;

import com.udacity.critter.data.Schedule;
import com.udacity.critter.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public void save(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
}

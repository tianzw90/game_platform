package com.platform.dnf.service.impl;

import com.platform.common.service.BaseServiceImpl;
import com.platform.dnf.entity.DnfFestival;
import com.platform.dnf.mapper.DnfFestivalMapper;
import com.platform.dnf.service.DnfFestivalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DnfFestivalServiceImpl extends BaseServiceImpl<DnfFestivalMapper, DnfFestival> implements DnfFestivalService {
    private final static Logger logger = LoggerFactory.getLogger(DnfFestivalServiceImpl.class);
}

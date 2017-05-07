package com.dev.challenge.service;

import com.dev.challenge.model.common.LinkBuilder;
import com.dev.challenge.util.Generator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zinoviyzubko on 07.05.17.
 */
public abstract class ParentService {

    @Autowired protected LinkBuilder linkBuilder;
    @Autowired protected Generator generator;
}

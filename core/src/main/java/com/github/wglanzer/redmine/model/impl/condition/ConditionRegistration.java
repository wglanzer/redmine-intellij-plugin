package com.github.wglanzer.redmine.model.impl.condition;

import com.github.wglanzer.redmine.model.EConditionOperator;
import de.adito.picoservice.PicoService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for conditions.
 * Uses PicoService as framework for loading
 *
 * @author w.glanzer, 25.02.2017.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@PicoService
public @interface ConditionRegistration
{

  EConditionOperator operator();

}

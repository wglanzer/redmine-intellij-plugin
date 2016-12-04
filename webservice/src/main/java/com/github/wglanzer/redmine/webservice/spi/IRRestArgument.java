package com.github.wglanzer.redmine.webservice.spi;

import com.github.wglanzer.redmine.webservice.impl.RRestArgumentImpl;
import org.jetbrains.annotations.NotNull;

/**
 * @author w.glanzer, 04.12.2016.
 */
public interface IRRestArgument
{

  IRRestArgument PROJECT_ID = new RRestArgumentImpl().name("project_id");
  IRRestArgument UPDATED_ON = new RRestArgumentImpl().name("updated_on");

  /**
   * Sets the name of this argument
   *
   * @param pName new Name
   * @return a copy of this argument, name set to pName
   */
  IRRestArgument name(@NotNull String pName);

  /**
   * Sets the value of this argument
   *
   * @param pValue new value
   * @return a copy of this argument, value set to pValue
   */
  IRRestArgument value(String pValue);

  /**
   * @return Returns the name of this argument
   */
  String getName();

  /**
   * @return Returns the value of this argument
   */
  String getValue();

}

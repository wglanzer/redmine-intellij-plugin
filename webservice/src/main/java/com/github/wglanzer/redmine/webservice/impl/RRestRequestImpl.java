package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestArgument;
import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Impl for IRRestRequest
 *
 * @author w.glanzer, 27.11.2016.
 */
public class RRestRequestImpl implements IRRestRequest
{

  private final String subpage;
  private final String toplevelResult;
  private final ArrayList<IRRestArgument> arguments;

  public RRestRequestImpl()
  {
    subpage = null;
    toplevelResult = null;
    arguments = new ArrayList<>();
  }

  private RRestRequestImpl(String pSubpage, String pToplevelResult, ArrayList<IRRestArgument> pArguments)
  {
    subpage = pSubpage;
    toplevelResult = pToplevelResult;
    arguments = pArguments;
  }

  @Override
  public IRRestRequest subpage(@NotNull String pSubPage)
  {
    return new RRestRequestImpl(pSubPage, toplevelResult, arguments);
  }

  @Override
  public IRRestRequest resultTopLevel(@Nullable String pTopLevelResult)
  {
    return new RRestRequestImpl(subpage, pTopLevelResult, arguments);
  }

  @Override
  public IRRestRequest argument(@NotNull IRRestArgument pArgument)
  {
    ArrayList<IRRestArgument> oldArgs = new ArrayList<>();
    oldArgs.add(pArgument);
    return new RRestRequestImpl(subpage, toplevelResult, oldArgs);
  }

  @NotNull
  @Override
  public String getSubPage()
  {
    if(subpage == null)
      throw new NullPointerException("subpage is null");

    return subpage;
  }

  @Override
  public String getResultTopLevel()
  {
    return toplevelResult;
  }

  @NotNull
  @Override
  public ArrayList<IRRestArgument> getArguments()
  {
    return arguments;
  }

  @Override
  public boolean equals(Object pO)
  {
    if(this == pO) return true;
    if(pO == null || getClass() != pO.getClass()) return false;
    RRestRequestImpl that = (RRestRequestImpl) pO;
    return Objects.equal(subpage, that.subpage) &&
        Objects.equal(toplevelResult, that.toplevelResult) &&
        Objects.equal(arguments, that.arguments);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(subpage, toplevelResult, arguments);
  }
}

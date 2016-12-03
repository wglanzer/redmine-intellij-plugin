package com.github.wglanzer.redmine.webservice.impl;

import com.github.wglanzer.redmine.webservice.spi.IRRestRequest;
import com.google.common.base.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Impl for IRRestRequest
 *
 * @author w.glanzer, 27.11.2016.
 */
public class RRestRequestImpl implements IRRestRequest
{

  private final String subpage;
  private final String toplevelResult;
  private final Map<String, String> arguments;

  public RRestRequestImpl()
  {
    subpage = null;
    toplevelResult = null;
    arguments = new HashMap<>();
  }

  private RRestRequestImpl(String pSubpage, String pToplevelResult, Map<String, String> pArguments)
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
  public IRRestRequest argument(@NotNull String pArgument, @Nullable String pValue)
  {
    HashMap<String, String> oldArgs = new HashMap<>(arguments);
    if(pValue != null)
      oldArgs.put(pArgument, pValue);
    else
      oldArgs.remove(pArgument);
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
  public Map<String, String> getArguments()
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

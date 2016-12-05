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
  private final boolean pageable;

  public RRestRequestImpl()
  {
    subpage = null;
    toplevelResult = null;
    arguments = new ArrayList<>();
    pageable = false;
  }

  private RRestRequestImpl(String pSubpage, String pToplevelResult, ArrayList<IRRestArgument> pArguments, boolean pPageable)
  {
    subpage = pSubpage;
    toplevelResult = pToplevelResult;
    arguments = pArguments;
    pageable = pPageable;
  }

  @Override
  public RRestRequestImpl argument(@NotNull IRRestArgument pArgument)
  {
    ArrayList<IRRestArgument> oldArgs = new ArrayList<>(arguments);
    oldArgs.add(pArgument);
    return new RRestRequestImpl(subpage, toplevelResult, oldArgs, pageable);
  }

  @NotNull
  @Override
  public ArrayList<IRRestArgument> getArguments()
  {
    return arguments;
  }

  @Override
  public boolean isPageable()
  {
    return pageable;
  }

  /**
   * This method sets the subpage-ID
   * For example: "projects" to query "http://redmine.myurl.com/projects.json"
   *
   * @param pSubPage Subpage as string, or <tt>null</tt> to clear
   * @return a new instance-copy of this request
   */
  public RRestRequestImpl subpage(@NotNull String pSubPage)
  {
    return new RRestRequestImpl(pSubPage, toplevelResult, arguments, pageable);
  }

  /**
   * This method sets the result toplevel.
   * If the redmine server API answers with:
   * projects:
   *   project:
   *     ...
   *   project:
   *     ...
   *
   * you can set "projects" as result toplevel, so that "projects"
   * wont be shown in result-Stream within the connection
   *
   * @param pTopLevelResult toplevel as string, or <tt>null</tt> to clear
   * @return a new instance-copy of this request
   */
  public RRestRequestImpl resultTopLevel(@Nullable String pTopLevelResult)
  {
    return new RRestRequestImpl(subpage, pTopLevelResult, arguments, pageable);
  }

  /**
   * Sets the flag that indicates, if the request is pageable or not
   *
   * @param pPageable <tt>true</tt> if it is pageable
   * @return a new instance-copy of this request
   */
  public RRestRequestImpl pageable(boolean pPageable)
  {
    return new RRestRequestImpl(subpage, toplevelResult, arguments, pPageable);
  }

  @NotNull
  public String getSubPage()
  {
    if(subpage == null)
      throw new NullPointerException("subpage is null");

    return subpage;
  }

  public String getResultTopLevel()
  {
    return toplevelResult;
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

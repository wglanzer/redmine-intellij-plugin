package com.github.wglanzer.redmine.webservice.spi;

import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * Describes the result of a request
 *
 * @author w.glanzer, 04.12.2016.
 */
public interface IRRestResult
{

  /**
   * Returns all nodes that where returned by webservice-API
   *
   * @return a NEW stream for all received nodes
   */
  Stream<Node> getResultNodes();

  interface Node
  {
    /**
     * Returns the value of an argument set in this node
     *
     * @param pArgument Argument that should be read
     * @return the value, or <tt>null</tt> if the argument is not set
     */
    @Nullable
    String getValue(IRRestArgument pArgument);
  }

}

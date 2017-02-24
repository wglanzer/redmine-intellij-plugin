package com.github.wglanzer.redmine;

import de.adito.picoservice.IPicoRegistration;
import de.adito.picoservice.IPicoRegistry;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * PicoRegistry that uses the class loader from the RManager-class so that
 * the IDEA-ServiceLoader can find our plugin-services
 *
 * @author w.glanzer, 26.02.2017.
 */
public class RPicoRegistry implements IPicoRegistry
{

  public static final IPicoRegistry INSTANCE = new RPicoRegistry();

  private final ServiceLoader<IPicoRegistration> serviceLoader = ServiceLoader.load(IPicoRegistration.class, RManager.class.getClassLoader());

  @NotNull
  @Override
  public <C, A extends Annotation> Map<Class<? extends C>, A> find(@NotNull Class<C> pSearchedType,
                                                                   @NotNull Class<A> pAnnotationClass)
  {
    Map<Class<? extends C>, A> map = new HashMap<>();
    for(IPicoRegistration registration : serviceLoader)
    {
      Class<?> annotatedClass = registration.getAnnotatedClass();
      if(pSearchedType.isAssignableFrom(annotatedClass))
      {
        Annotation annotation = annotatedClass.getAnnotation(pAnnotationClass);
        if(annotation != null)
          //noinspection unchecked
          map.put((Class<C>) annotatedClass, (A) annotation);
      }
    }
    return map;
  }


  @NotNull
  @Override
  public <T, C> Stream<T> find(@NotNull Class<C> pSearchedType,
                               @NotNull Function<Class<? extends C>, T> pResolverFunction)
  {
    Stream.Builder<T> streamBuilder = Stream.builder();
    for(IPicoRegistration registration : serviceLoader)
    {
      Class<?> annotatedClass = registration.getAnnotatedClass();
      if(pSearchedType.isAssignableFrom(annotatedClass))
      {
        @SuppressWarnings("unchecked")
        T result = pResolverFunction.apply((Class<? extends C>) annotatedClass);
        if(result != null)
          streamBuilder.add(result);
      }
    }
    return streamBuilder.build();
  }

}

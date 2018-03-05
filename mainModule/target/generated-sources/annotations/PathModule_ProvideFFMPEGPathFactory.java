import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class PathModule_ProvideFFMPEGPathFactory implements Factory<String> {
  private final PathModule module;

  public PathModule_ProvideFFMPEGPathFactory(PathModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public String get() {  
    String provided = module.provideFFMPEGPath();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<String> create(PathModule module) {  
    return new PathModule_ProvideFFMPEGPathFactory(module);
  }
}


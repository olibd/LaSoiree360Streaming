import ComputerModule.OSType;
import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class ComputerModule_ProvideOSTypeFactory implements Factory<OSType> {
  private final ComputerModule module;

  public ComputerModule_ProvideOSTypeFactory(ComputerModule module) {  
    assert module != null;
    this.module = module;
  }

  @Override
  public OSType get() {  
    OSType provided = module.provideOSType();
    if (provided == null) {
      throw new NullPointerException("Cannot return null from a non-@Nullable @Provides method");
    }
    return provided;
  }

  public static Factory<OSType> create(ComputerModule module) {  
    return new ComputerModule_ProvideOSTypeFactory(module);
  }
}


import ComputerModule.OSType;
import dagger.MembersInjector;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated("dagger.internal.codegen.ComponentProcessor")
public final class PathModule_MembersInjector implements MembersInjector<PathModule> {
  private final Provider<OSType> osTypeProvider;

  public PathModule_MembersInjector(Provider<OSType> osTypeProvider) {  
    assert osTypeProvider != null;
    this.osTypeProvider = osTypeProvider;
  }

  @Override
  public void injectMembers(PathModule instance) {  
    if (instance == null) {
      throw new NullPointerException("Cannot inject members into a null reference");
    }
    instance.osType = osTypeProvider.get();
  }

  public static MembersInjector<PathModule> create(Provider<OSType> osTypeProvider) {  
      return new PathModule_MembersInjector(osTypeProvider);
  }
}


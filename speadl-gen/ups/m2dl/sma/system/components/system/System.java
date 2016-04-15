package ups.m2dl.sma.system.components.system;

import ups.m2dl.sma.system.components.gui.Interface;
import ups.m2dl.sma.system.components.university.University;
import ups.m2dl.system.components.environment.Environment;
import ups.m2dl.system.components.services.Services;

@SuppressWarnings("all")
public abstract class System {
  public interface Requires {
  }
  
  public interface Component extends System.Provides {
  }
  
  public interface Provides {
  }
  
  public interface Parts {
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Environment.Component environment();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Services.Component services();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public University.Component university();
    
    /**
     * This can be called by the implementation to access the part and its provided ports.
     * It will be initialized after the required ports are initialized and before the provided ports are initialized.
     * 
     */
    public Interface.Component interfaces();
  }
  
  public static class ComponentImpl implements System.Component, System.Parts {
    private final System.Requires bridge;
    
    private final System implementation;
    
    public void start() {
      assert this.environment != null: "This is a bug.";
      ((Environment.ComponentImpl) this.environment).start();
      assert this.services != null: "This is a bug.";
      ((Services.ComponentImpl) this.services).start();
      assert this.university != null: "This is a bug.";
      ((University.ComponentImpl) this.university).start();
      assert this.interfaces != null: "This is a bug.";
      ((Interface.ComponentImpl) this.interfaces).start();
      this.implementation.start();
      this.implementation.started = true;
    }
    
    private void init_environment() {
      assert this.environment == null: "This is a bug.";
      assert this.implem_environment == null: "This is a bug.";
      this.implem_environment = this.implementation.make_environment();
      if (this.implem_environment == null) {
      	throw new RuntimeException("make_environment() in ups.m2dl.sma.system.components.system.System should not return null.");
      }
      this.environment = this.implem_environment._newComponent(new BridgeImpl_environment(), false);
      
    }
    
    private void init_services() {
      assert this.services == null: "This is a bug.";
      assert this.implem_services == null: "This is a bug.";
      this.implem_services = this.implementation.make_services();
      if (this.implem_services == null) {
      	throw new RuntimeException("make_services() in ups.m2dl.sma.system.components.system.System should not return null.");
      }
      this.services = this.implem_services._newComponent(new BridgeImpl_services(), false);
      
    }
    
    private void init_university() {
      assert this.university == null: "This is a bug.";
      assert this.implem_university == null: "This is a bug.";
      this.implem_university = this.implementation.make_university();
      if (this.implem_university == null) {
      	throw new RuntimeException("make_university() in ups.m2dl.sma.system.components.system.System should not return null.");
      }
      this.university = this.implem_university._newComponent(new BridgeImpl_university(), false);
      
    }
    
    private void init_interfaces() {
      assert this.interfaces == null: "This is a bug.";
      assert this.implem_interfaces == null: "This is a bug.";
      this.implem_interfaces = this.implementation.make_interfaces();
      if (this.implem_interfaces == null) {
      	throw new RuntimeException("make_interfaces() in ups.m2dl.sma.system.components.system.System should not return null.");
      }
      this.interfaces = this.implem_interfaces._newComponent(new BridgeImpl_interfaces(), false);
      
    }
    
    protected void initParts() {
      init_environment();
      init_services();
      init_university();
      init_interfaces();
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final System implem, final System.Requires b, final boolean doInits) {
      this.bridge = b;
      this.implementation = implem;
      
      assert implem.selfComponent == null: "This is a bug.";
      implem.selfComponent = this;
      
      // prevent them to be called twice if we are in
      // a specialized component: only the last of the
      // hierarchy will call them after everything is initialised
      if (doInits) {
      	initParts();
      	initProvidedPorts();
      }
    }
    
    private Environment.Component environment;
    
    private Environment implem_environment;
    
    private final class BridgeImpl_environment implements Environment.Requires {
    }
    
    public final Environment.Component environment() {
      return this.environment;
    }
    
    private Services.Component services;
    
    private Services implem_services;
    
    private final class BridgeImpl_services implements Services.Requires {
    }
    
    public final Services.Component services() {
      return this.services;
    }
    
    private University.Component university;
    
    private University implem_university;
    
    private final class BridgeImpl_university implements University.Requires {
    }
    
    public final University.Component university() {
      return this.university;
    }
    
    private Interface.Component interfaces;
    
    private Interface implem_interfaces;
    
    private final class BridgeImpl_interfaces implements Interface.Requires {
    }
    
    public final Interface.Component interfaces() {
      return this.interfaces;
    }
  }
  
  /**
   * Used to check that two components are not created from the same implementation,
   * that the component has been started to call requires(), provides() and parts()
   * and that the component is not started by hand.
   * 
   */
  private boolean init = false;;
  
  /**
   * Used to check that the component is not started by hand.
   * 
   */
  private boolean started = false;;
  
  private System.ComponentImpl selfComponent;
  
  /**
   * Can be overridden by the implementation.
   * It will be called automatically after the component has been instantiated.
   * 
   */
  protected void start() {
    if (!this.init || this.started) {
    	throw new RuntimeException("start() should not be called by hand: to create a new component, use newComponent().");
    }
  }
  
  /**
   * This can be called by the implementation to access the provided ports.
   * 
   */
  protected System.Provides provides() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This can be called by the implementation to access the required ports.
   * 
   */
  protected System.Requires requires() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("requires() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if requires() is needed to initialise the component.");
    }
    return this.selfComponent.bridge;
  }
  
  /**
   * This can be called by the implementation to access the parts and their provided ports.
   * 
   */
  protected System.Parts parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Environment make_environment();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Services make_services();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract University make_university();
  
  /**
   * This should be overridden by the implementation to define how to create this sub-component.
   * This will be called once during the construction of the component to initialize this sub-component.
   * 
   */
  protected abstract Interface make_interfaces();
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized System.Component _newComponent(final System.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of System has already been used to create a component, use another one.");
    }
    this.init = true;
    System.ComponentImpl  _comp = new System.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public System.Component newComponent() {
    return this._newComponent(new System.Requires() {}, true);
  }
}

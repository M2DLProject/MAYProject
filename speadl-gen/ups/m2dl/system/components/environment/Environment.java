package ups.m2dl.system.components.environment;

import ups.m2dl.sma.system.environnement.interfaces.IgetRoom;
import ups.m2dl.sma.system.environnement.interfaces.IsetRoom;

@SuppressWarnings("all")
public abstract class Environment {
  public interface Requires {
  }
  
  public interface Component extends Environment.Provides {
  }
  
  public interface Provides {
  }
  
  public interface Parts {
  }
  
  public static class ComponentImpl implements Environment.Component, Environment.Parts {
    private final Environment.Requires bridge;
    
    private final Environment implementation;
    
    public void start() {
      this.implementation.start();
      this.implementation.started = true;
    }
    
    protected void initParts() {
      
    }
    
    protected void initProvidedPorts() {
      
    }
    
    public ComponentImpl(final Environment implem, final Environment.Requires b, final boolean doInits) {
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
  }
  
  public static abstract class Room {
    public interface Requires {
    }
    
    public interface Component extends Environment.Room.Provides {
    }
    
    public interface Provides {
      /**
       * This can be called to access the provided port.
       * 
       */
      public IgetRoom getRoom();
      
      /**
       * This can be called to access the provided port.
       * 
       */
      public IsetRoom setRoom();
    }
    
    public interface Parts {
    }
    
    public static class ComponentImpl implements Environment.Room.Component, Environment.Room.Parts {
      private final Environment.Room.Requires bridge;
      
      private final Environment.Room implementation;
      
      public void start() {
        this.implementation.start();
        this.implementation.started = true;
      }
      
      protected void initParts() {
        
      }
      
      private void init_getRoom() {
        assert this.getRoom == null: "This is a bug.";
        this.getRoom = this.implementation.make_getRoom();
        if (this.getRoom == null) {
        	throw new RuntimeException("make_getRoom() in ups.m2dl.system.components.environment.Environment$Room should not return null.");
        }
      }
      
      private void init_setRoom() {
        assert this.setRoom == null: "This is a bug.";
        this.setRoom = this.implementation.make_setRoom();
        if (this.setRoom == null) {
        	throw new RuntimeException("make_setRoom() in ups.m2dl.system.components.environment.Environment$Room should not return null.");
        }
      }
      
      protected void initProvidedPorts() {
        init_getRoom();
        init_setRoom();
      }
      
      public ComponentImpl(final Environment.Room implem, final Environment.Room.Requires b, final boolean doInits) {
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
      
      private IgetRoom getRoom;
      
      public IgetRoom getRoom() {
        return this.getRoom;
      }
      
      private IsetRoom setRoom;
      
      public IsetRoom setRoom() {
        return this.setRoom;
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
    
    private Environment.Room.ComponentImpl selfComponent;
    
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
    protected Environment.Room.Provides provides() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("provides() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if provides() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    /**
     * This should be overridden by the implementation to define the provided port.
     * This will be called once during the construction of the component to initialize the port.
     * 
     */
    protected abstract IgetRoom make_getRoom();
    
    /**
     * This should be overridden by the implementation to define the provided port.
     * This will be called once during the construction of the component to initialize the port.
     * 
     */
    protected abstract IsetRoom make_setRoom();
    
    /**
     * This can be called by the implementation to access the required ports.
     * 
     */
    protected Environment.Room.Requires requires() {
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
    protected Environment.Room.Parts parts() {
      assert this.selfComponent != null: "This is a bug.";
      if (!this.init) {
      	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
      }
      return this.selfComponent;
    }
    
    /**
     * Not meant to be used to manually instantiate components (except for testing).
     * 
     */
    public synchronized Environment.Room.Component _newComponent(final Environment.Room.Requires b, final boolean start) {
      if (this.init) {
      	throw new RuntimeException("This instance of Room has already been used to create a component, use another one.");
      }
      this.init = true;
      Environment.Room.ComponentImpl  _comp = new Environment.Room.ComponentImpl(this, b, true);
      if (start) {
      	_comp.start();
      }
      return _comp;
    }
    
    private Environment.ComponentImpl ecosystemComponent;
    
    /**
     * This can be called by the species implementation to access the provided ports of its ecosystem.
     * 
     */
    protected Environment.Provides eco_provides() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
    }
    
    /**
     * This can be called by the species implementation to access the required ports of its ecosystem.
     * 
     */
    protected Environment.Requires eco_requires() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent.bridge;
    }
    
    /**
     * This can be called by the species implementation to access the parts of its ecosystem and their provided ports.
     * 
     */
    protected Environment.Parts eco_parts() {
      assert this.ecosystemComponent != null: "This is a bug.";
      return this.ecosystemComponent;
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
  
  private Environment.ComponentImpl selfComponent;
  
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
  protected Environment.Provides provides() {
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
  protected Environment.Requires requires() {
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
  protected Environment.Parts parts() {
    assert this.selfComponent != null: "This is a bug.";
    if (!this.init) {
    	throw new RuntimeException("parts() can't be accessed until a component has been created from this implementation, use start() instead of the constructor if parts() is needed to initialise the component.");
    }
    return this.selfComponent;
  }
  
  /**
   * Not meant to be used to manually instantiate components (except for testing).
   * 
   */
  public synchronized Environment.Component _newComponent(final Environment.Requires b, final boolean start) {
    if (this.init) {
    	throw new RuntimeException("This instance of Environment has already been used to create a component, use another one.");
    }
    this.init = true;
    Environment.ComponentImpl  _comp = new Environment.ComponentImpl(this, b, true);
    if (start) {
    	_comp.start();
    }
    return _comp;
  }
  
  /**
   * This should be overridden by the implementation to instantiate the implementation of the species.
   * 
   */
  protected abstract Environment.Room make_Room(final int Identifier);
  
  /**
   * Do not call, used by generated code.
   * 
   */
  public Environment.Room _createImplementationOfRoom(final int Identifier) {
    Environment.Room implem = make_Room(Identifier);
    if (implem == null) {
    	throw new RuntimeException("make_Room() in ups.m2dl.system.components.environment.Environment should not return null.");
    }
    assert implem.ecosystemComponent == null: "This is a bug.";
    assert this.selfComponent != null: "This is a bug.";
    implem.ecosystemComponent = this.selfComponent;
    return implem;
  }
  
  /**
   * This can be called to create an instance of the species from inside the implementation of the ecosystem.
   * 
   */
  protected Environment.Room.Component newRoom(final int Identifier) {
    Environment.Room _implem = _createImplementationOfRoom(Identifier);
    return _implem._newComponent(new Environment.Room.Requires() {},true);
  }
  
  /**
   * Use to instantiate a component from this implementation.
   * 
   */
  public Environment.Component newComponent() {
    return this._newComponent(new Environment.Requires() {}, true);
  }
}

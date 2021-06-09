import com.kauruck.coastEngine.centum.component.AbstractComponent;
import com.kauruck.coastEngine.centum.system.AbstractSystem;

public class TestSystem extends AbstractSystem<TestComponent> {
    public TestSystem() {
        super(10, TestComponent.class);
    }

    @Override
    public void process(AbstractComponent component) {
        if(component instanceof TestComponent){
            TestComponent comp = (TestComponent) component;
            System.out.println(comp.test);
            comp.test++;
        }
    }
}

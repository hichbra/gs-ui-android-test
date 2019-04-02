package ui.graphstream.org.gs_ui_androidtestFull;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.ViewerListener;

public class Activity_TestPies extends FragmentActivity {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;

    private double[] values = new double[3];
    private Node A, B ;

    SpriteManager sm;
    Sprite pie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        SingleGraph g = new SingleGraph("test");
        A = g.addNode("A");
        B = g.addNode("B");
        g.addEdge("AB", "A", "B");

        sm = new SpriteManager(g);
        pie = sm.addSprite("pie");

        pie.setAttribute("ui.style", "shape: pie-chart; fill-color: #F00, #0F0, #00F; size: 500px;");

        values[0] = 0.3333;
        values[1] = 0.3333;
        values[2] = 0.3333;
        pie.setAttribute("ui.pie-values", values);
        pie.attachToEdge("AB");
        pie.setPosition(0.5);

        g.setAttribute("ui.antialias");

        display(savedInstanceState, g, true);
    }

    /**
     * In onStart, the AndroidViewer is already created
     */
    @Override
    protected void onStart() {
        super.onStart();

        new Thread( () ->  {
            double[] values2 = new double[3];
            values2[0] = 0.1;
            values2[1] = 0.3;
            values2[2] = 0.6;
            boolean on = true;

            while( true ) {
                sleep( 2000 );

                if(on) {
                    values[0] = 0.1;
                    values[1] = 0.3;
                    values[2] = 0.6;
                    A.setAttribute("ui.pie-values", new double[]{1.0});
                    A.setAttribute("ui.style", "shape:pie-chart; fill-color:red;size: 80px;");
                }
                else {
                    values[0] = 0.3;
                    values[1] = 0.3;
                    values[2] = 0.3;
                    A.setAttribute("ui.pie-values", new double[]{1.0});
                    A.setAttribute("ui.style", "shape:pie-chart; fill-color:blue;size: 80px;");
                }
                pie.setAttribute("ui.pie-values", values);

                //pie.addAttribute("ui.pie-values", if(on) values else values2)
                on = ! on;
            }
        }).start();
    }

    public void display(Bundle savedInstanceState, Graph graph, boolean autoLayout) {
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();

            // find fragment or create him
            fragment = (DefaultFragment) fm.findFragmentByTag("fragment_tag");
            if (null == fragment) {
                fragment = new DefaultFragment();
                fragment.init(graph, autoLayout);
            }

            // Add the fragment in the layout and commit
            FragmentTransaction ft = fm.beginTransaction() ;
            ft.add(CONTENT_VIEW_ID, fragment).commit();
        }
    }

    protected void sleep( long ms ) {
        try {
            Thread.sleep( ms );
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}

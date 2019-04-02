package ui.graphstream.org.gs_ui_androidtestFull;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;

public class Activity_TestPolys extends FragmentActivity {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        SingleGraph g = new SingleGraph("Polys");

        Node A = g.addNode("A");
        Node B = g.addNode("B");
        Node C = g.addNode("C");
        Node D = g.addNode("D");

        A.setAttribute("xyz", new double[]{  1,  1, 0});
        B.setAttribute("xyz", new double[]{  1,  -1, 0});
        C.setAttribute("xyz", new double[]{  -1,  -1, 0});
        D.setAttribute("xyz", new double[]{  -1,  1, 0});

        A.setAttribute("ui.label", "A");
        B.setAttribute("ui.label", "B");
        C.setAttribute("ui.label", "C");
        D.setAttribute("ui.label", "D");

        Edge AB = g.addEdge("AB", "A", "B");
        Edge BC = g.addEdge("BC", "B", "C");
        Edge CD = g.addEdge("CD", "C", "D");
        Edge DA = g.addEdge("DA", "D", "A");


        AB.setAttribute("ui.points", new double[]{1, 1, 0,
                1.25, 0.5, 0,
                0.75, -0.5, 0,
                1, -1, 0});
        BC.setAttribute("ui.points", new double[]{1, -1, 0,
                0.5, -0.5, 0,
                -0.5, -0.25, 0,
                -1, -1, 0});
        CD.setAttribute("ui.points", new double[]{-1, -1, 0,
                -0.40, -0.5, 0,
                -1.70, 0.5, 0,
                -1, 1, 0});
        //DA.setAttribute("ui.points", new double[]{-1, 1, 0,
        //                -0.5, 0.75, 0,
        //                0.5, 0.25, 0,
        //                1, 1, 0});

        g.setAttribute("ui.stylesheet", styleSheet);
        g.setAttribute("ui.antialias");

        display(savedInstanceState, g, false);
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

    String styleSheet = "node { size: 50;} edge { shape: cubic-curve; size: 10, 10; }";
}

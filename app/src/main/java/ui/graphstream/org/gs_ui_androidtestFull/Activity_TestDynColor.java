package ui.graphstream.org.gs_ui_androidtestFull;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestDynColor extends FragmentActivity {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        Graph g = new MultiGraph("Test Stroke");
        g.setAttribute("ui.antialias");
        g.setAttribute("ui.stylesheet", "node { fill-mode: dyn-plain; stroke-mode: plain; stroke-width: 3px; size: 60px;} edge { fill-mode: dyn-plain; size: 3px;}");
        g.addNode("A"); g.addNode("B"); g.addNode("C");
        g.addEdge("AB", "A", "B"); g.addEdge("BC", "B", "C"); g.addEdge("CA", "C", "A");

        g.getNode("A").setAttribute("ui.color", Color.RED);
        g.getNode("B").setAttribute("ui.color", Color.GREEN);
        g.getNode("C").setAttribute("ui.color", Color.BLUE);
        g.getEdge("AB").setAttribute("ui.color", Color.YELLOW);
        g.getEdge("BC").setAttribute("ui.color", Color.MAGENTA);
        g.getEdge("CA").setAttribute("ui.color", Color.CYAN);

        display(savedInstanceState, g, true);
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
}

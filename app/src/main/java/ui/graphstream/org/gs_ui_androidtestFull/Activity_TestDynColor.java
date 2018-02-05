package ui.graphstream.org.gs_ui_androidtestFull;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestDynColor extends Activity {

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
        g.setAttribute("ui.stylesheet", styleSheet);

        g.setAttribute("ui.stylesheet", "node { fill-mode: dyn-plain; stroke-mode: plain; stroke-width: 1px; } edge { fill-mode: dyn-plain; }");
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
            FragmentManager fm = getFragmentManager();

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

    private String styleSheet =
            "node {"+
                    "	fill-color: white;"+
                    "	fill-mode: plain;"+
                    "	stroke-mode: dashes;"+
                    "	stroke-width: 1px;"+
                    "	stroke-color: red;"+
                    "	size: 20px;"+
                    "}"+
                    "node#A {"+
                    "	shape: triangle;"+
                    "	stroke-mode: plain;"+
                    "}"+
                    "node#B {"+
                    "	shape: cross;"+
                    "	stroke-mode: plain;"+
                    "}"+
                    "node#C {"+
                    "	shape: diamond;"+
                    "	stroke-mode: plain;"+
                    "}"+
                    "node#D {"+
                    "	fill-color: gray; "+
                    "	stroke-color: blue; "+
                    "}"+
                    "edge {"+
                    //"	fill-mode: none;"+
                    "	shape: line;"+
                    "	size: 0px;"+
                    "	stroke-mode: dashes;"+
                    "	stroke-width: 1px;"+
                    "	stroke-color: red;"+
                    "	fill-color: red;"+
                    "}"+
                    "edge#BC {"+
                    "	shape: blob; size: 3px; fill-color: #444;"+
                    "}"+
                    "edge#AD {"+
                    "	stroke-mode: double;"+
                    "}";
}

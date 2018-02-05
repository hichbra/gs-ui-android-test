package ui.graphstream.org.gs_ui_androidtestFull;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestStrokeMode extends Activity {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private Graph graph ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new MultiGraph("Test Stroke");
        graph.setAttribute("ui.antialias");
        graph.setAttribute("ui.stylesheet", styleSheet);

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.addEdge("AD", "A", "D");
        graph.forEach(node -> node.setAttribute("ui.label", node.getId()));

        display(savedInstanceState, graph, true);
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
                    "	stroke-width: 5px;"+
                    "	stroke-color: red;"+
                    "	size: 60px;"+
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
                    "	size: 5px;"+
                    "	stroke-mode: dashes;"+
                    "	stroke-width: 8px;"+
                    "	stroke-color: red;"+
                    "	fill-color: red;"+
                    "}"+
                    "edge#BC {"+
                    "	shape: blob; size: 10px; fill-color: #444;"+
                    "}"+
                    "edge#AD {"+
                    "	stroke-mode: double;"+
                    "}";
}

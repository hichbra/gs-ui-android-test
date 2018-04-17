package ui.graphstream.org.gs_ui_androidtest;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;

public class Activity_withoutXML extends AppCompatActivity {

    private static final int CONTENT_VIEW_ID = 10101010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        Graph graph = new SingleGraph("Tutorial 1");

        graph.setAttribute("ui.stylesheet", "" +
                "graph { padding: 0px; fill-color: #EEE; }"+
                "edge { text-size: 48; size: 5;}"+
                "node { text-size: 48; fill-color: white; size: 40px, 40px; padding: 15px, 15px; stroke-mode: plain; stroke-color: #555; shape: box; }"
                + "node#B {shape: circle;}"
        );

        graph.setAttribute("ui.antialias");

        graph.setStrict(false);
        graph.setAutoCreate( true );
        Edge e = graph.addEdge( "AB", "B", "A", true);
        e.setAttribute("weight", 5.0);
        e = graph.addEdge( "BC", "B", "C", true);

        Node b = graph.getNode("B");
        b.setAttribute("label", "B");

        e.setAttribute("weight", 1.0);
        e = graph.addEdge( "CA", "C", "A", true);
        e.setAttribute("weight", 2.0);
        e = graph.addEdge( "BF", "F", "B", true);
        e.setAttribute("weight", 1.0);
        e = graph.addEdge( "CE", "E", "C", true);
        e.setAttribute("weight", 8.0);
        e = graph.addEdge( "EF", "E", "F", true);
        e.setAttribute("weight", 1.0);

        for(int i = 0 ; i < graph.getNodeCount() ; i++){
            graph.getNode(i).setAttribute("ui.label", graph.getNode(i).getId());
        }

        for(int i = 0 ; i < graph.getEdgeCount() ; i++){
            graph.getEdge(i).setAttribute("ui.label", "Poids = "+(graph.getEdge(i).getAttribute("weight")));
        }

        Log.e("Debug","BEFORE");

        display(savedInstanceState, graph, true);

        Log.e("Debug", "AFTER");
    }

    public void display(Bundle savedInstanceState, Graph graph, boolean autoLayout) {
         if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            DefaultFragment fragment = (DefaultFragment) fm.findFragmentByTag("fragment_tag");

            if (null == fragment) {
                fragment = new DefaultFragment();
                fragment.init(graph, autoLayout);
            }

            FragmentTransaction ft = fm.beginTransaction() ;
            ft.add(CONTENT_VIEW_ID, fragment).commit();
        }
    }
}

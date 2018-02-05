package ui.graphstream.org.gs_ui_androidtest;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public class Activity_ColorInterpolation extends Activity implements ViewerListener {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private Graph graph ;
    protected boolean loop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new MultiGraph("Color Interpolation");
        graph.setAttribute("ui.antialias");

        display(savedInstanceState, graph, true);
    }

    /**
     * In onStart, the AndroidViewer is already created
     */
    @Override
    protected void onStart() {
        super.onStart();

        ViewerPipe pipe = fragment.getViewer().newViewerPipe();
        pipe.addViewerListener(this);

        Node A = graph.addNode("A");
        Node B = graph.addNode("B");
        Node C = graph.addNode("C");

        graph.addEdge("AB", "A", "B", true);
        graph.addEdge("BC", "B", "C", true);
        graph.addEdge("CA", "C", "A", true);

        A.setAttribute("xyz", 0, 1, 0);
        B.setAttribute("xyz", 1, 0, 0);
        C.setAttribute("xyz", -1, 0, 0);

        graph.setAttribute("ui.stylesheet", styleSheet);

        new Thread( () ->  {
            float color = 0;
            float dir = 0.01f;

              while (loop) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                pipe.pump();

                color += dir;

                if (color > 1) {
                    color = 1;
                    dir = -dir;
                } else if (color < 0) {
                    color = 0;
                    dir = -dir;
                }

                A.setAttribute("ui.color", color);
                showSelection(graph);
            }

            System.exit(0);
        }).start();
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

    protected void showSelection(Graph graph) {
        boolean selection = false;
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (Node node : graph) {
            if (node.hasAttribute("ui.selected")) {
                sb.append(String.format(" %s", node.getId()));
                selection = true;
            }
            if (node.hasAttribute("ui.clicked")) {
                Log.e("Debug", "node "+node.getId()+" clicked ");
            }
        }

        sb.append(" ]");

        if (selection)
            Log.e("Debug", "selection = "+sb.toString());
    }

    protected static String styleSheet = "graph { padding: 20px; stroke-width: 0px; }"
            + "node {size: 48,48;}"
            + "node:selected { fill-color: red;  fill-mode: plain; }"
            + "node:clicked  { fill-color: blue; fill-mode: plain; }"
            + "node#A        { fill-color: green, yellow, purple; fill-mode: dyn-plain; }"
            + "edge{ size: 5;}";

    public void buttonPushed(String id) {
    }

    public void buttonReleased(String id) {
    }

    public void mouseOver(String id) {
    }

    public void mouseLeft(String id) {
    }

    public void viewClosed(String viewName) {
        loop = false;
    }
}

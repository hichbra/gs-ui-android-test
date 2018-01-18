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
import org.graphstream.ui.android_viewer.AndroidViewer;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.android_viewer.util.DefaultMouseManager;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;
import org.graphstream.ui.view.util.InteractiveElement;

import java.util.EnumSet;

public class Activity_EdgeSelection extends Activity implements ViewerListener {

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

        graph = new MultiGraph("Edge Selection");

        display(savedInstanceState, graph, true);
    }

    /**
     * In onStart, the AndroidViewer is already created
     */
    @Override
    protected void onStart() {
        super.onStart();

        AndroidViewer viewer = fragment.getViewer();
        viewer.getDefaultView().setMouseManager(new DefaultMouseManager(EnumSet.of(InteractiveElement.EDGE, InteractiveElement.NODE, InteractiveElement.SPRITE)));
        ViewerPipe pipe = viewer.newViewerPipe();

        graph.setAttribute( "ui.quality" );
        graph.setAttribute("ui.antialias");

        pipe.addViewerListener(new Activity_EdgeSelection());

        for (String nodeId : new String[]{"A", "B", "C"}) {
            Node node = graph.addNode(nodeId);
            node.setAttribute("ui.label", nodeId);

        }

        graph.addEdge("AB", "A", "B", true);
        graph.addEdge("BC", "B", "C", true);
        graph.addEdge("CA", "C", "A", true);

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

                  showSelection(graph);
            }
        }).start();
    }

    public void display(Bundle savedInstanceState, Graph graph, boolean autoLayout) {
        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();

            // find fragment or create him
            fragment = (DefaultFragment) fm.findFragmentByTag("fragment_tag");
            if (null == fragment) {
                DefaultFragment.autoLayout = autoLayout ;
                fragment = new DefaultFragment();
                fragment.init(graph);
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
            + "node {size: 48, 48;}"
            + "node:selected { fill-color: red;  fill-mode: plain; }"
            + "node:clicked  { fill-color: blue; fill-mode: plain; }"
            + "edge{ size: 5;}"
            + "edge:selected { fill-color: purple; fill-mode: plain; }"
            + "edge:clicked  { fill-color: orange; fill-mode: plain; }"
            + "node#A        { fill-color: green, yellow, purple; fill-mode: dyn-plain; }";

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

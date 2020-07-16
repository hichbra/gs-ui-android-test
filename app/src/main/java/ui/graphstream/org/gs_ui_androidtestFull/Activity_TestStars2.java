package ui.graphstream.org.gs_ui_androidtestFull;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.GraphPosLengthUtils;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.util.Random;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestStars2 extends FragmentActivity {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private Graph graph ;
    protected boolean loop = true;

    public static final int URL_IMAGE = R.drawable.icon ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new SingleGraph("Test Stars 2");
        graph.setAttribute("ui.antialias");

        display(savedInstanceState, graph, false);
    }

    /**
     * In onStart, the AndroidViewer is already created
     */
    @Override
    protected void onStart() {
        super.onStart();

        double x0 = 0.0;
        double x1 = 0.0;
        double width = 100.0;
        double height = 20.0;
        int n = 500;
        Random random = new Random();
        double minDis = 4.0;
        double sizeMx = 10.0;

        graph.setAttribute("ui.stylesheet", styleSheet);
        graph.setAttribute("ui.antialias");

        for (int i = 0; i < n; i++) {
            Node node = graph.addNode(i + "");
            node.setAttribute("xyz", (random.nextDouble() * width), (random.nextDouble() * height), 0);
            node.setAttribute("ui.size", (random.nextDouble() * sizeMx));
        }

        graph.nodes().forEach(node -> {
            Point3 pos = new Point3(GraphPosLengthUtils.nodePosition(node));

            graph.nodes().forEach(otherNode -> {
                if (otherNode != node) {
                    Point3 otherPos = new Point3(GraphPosLengthUtils.nodePosition(otherNode));
                    double dist = otherPos.distance(pos);

                    if (dist < minDis) {
                        if (!node.hasEdgeBetween(otherNode.getId())) {
                            try {
                                graph.addEdge(node.getId() + "--" + otherNode.getId(), node.getId(), otherNode.getId());
                            } catch (IdAlreadyInUseException e) {
                                graph.addEdge(node.getId() + "--" + otherNode.getId() + "-2", node.getId(), otherNode.getId());
                            }
                        }
                    }
                }

            });
        });
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

    private String styleSheet =
            "graph {"+
                    "	canvas-color: black;"+
                    "	fill-mode: gradient-vertical;"+
                    "	fill-color: black, #004;"+
                    "	padding: 20px;"+
                    "}"+
                    "node {"+
                    "	shape: circle;"+
                    "	size-mode: dyn-size;"+
                    "	size: 60px;"+
                    "	fill-mode: gradient-radial;"+
                    "	fill-color: #FFFC, #FFF0;"+
                    "	stroke-mode: none;"+
                    "	shadow-mode: gradient-radial;"+
                    "	shadow-color: #FFF5, #FFF0;"+
                    "	shadow-width: 5px;"+
                    "	shadow-offset: 0px, 0px;"+
                    "}"+
                    "node:clicked {"+
                    "	fill-color: #F00A, #F000;"+
                    "}"+
                    "node:selected {"+
                    "	fill-color: #00FA, #00F0;"+
                    "}"+
                    "edge {"+
                    "	shape: L-square-line;"+
                    "	size: 3px;"+
                    "	fill-color: #FFF3;"+
                    "	fill-mode: plain;"+
                    "	arrow-shape: none;"+
                    "}"+
                    "sprite {"+
                    "	shape: circle;"+
                    "	fill-mode: gradient-radial;"+
                    "	fill-color: #FFF8, #FFF0;"+
                    "}";
}

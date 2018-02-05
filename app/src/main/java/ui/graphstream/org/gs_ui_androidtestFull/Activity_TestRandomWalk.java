package ui.graphstream.org.gs_ui_androidtestFull;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.randomWalk.RandomWalk;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;

public class Activity_TestRandomWalk extends Activity {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private MultiGraph graph ;
    protected boolean loop = true;

    DorogovtsevMendesGenerator gen ;
    RandomWalk rwalk ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new MultiGraph("random walk");
        graph.setAttribute("ui.antialias");

        gen = new DorogovtsevMendesGenerator();
        rwalk = new RandomWalk();

        display(savedInstanceState, graph, true);
    }

    /**
     * In onStart, the AndroidViewer is already created
     */
    @Override
    protected void onStart() {
        super.onStart();

        graph.setAttribute("ui.stylesheet", styleSheet);

        gen.addSink(graph);
        gen.begin();
        for(int i = 0 ; i < 200 ; i++) {
            gen.nextEvents();
        }
        gen.end();

        new Thread(() -> {
            rwalk.setEntityCount(graph.getNodeCount() * 2);
            rwalk.setEvaporation(0.97);
            rwalk.setEntityMemory(40);
            rwalk.init(graph);

            for (int i = 0; i < 600; i++) {
                rwalk.compute();

                if (i % 10 == 0) {
                    Log.e("Debug", "step " + i);
                    updateGraph(graph, rwalk);
                }
            }
            rwalk.terminate();
            updateGraph(graph, rwalk);
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

    public String styleSheet =
            "edge {"+
                    "size: 5px;"+
                    "fill-color: red, yellow, green, #444;"+
                    "fill-mode: dyn-plain;"+
                    "}"+
                    "node {"+
                    "size: 30px;"+
                    "fill-color: #444;"+
                    "}";

    public void updateGraph(Graph graph, RandomWalk rwalk) {
        float mine[] = {Float.MAX_VALUE};
        float maxe[] = {Float.MIN_VALUE};

        graph.edges().forEach ( edge -> {
            float passes = (float) rwalk.getPasses(edge);
            if(passes>maxe[0])
                maxe[0] = passes;
            if(passes<mine[0])
                mine[0] = passes;
        });

        graph.edges().forEach ( edge -> {
            float passes = (float) rwalk.getPasses(edge);
            float color  = ((passes-mine[0])/(maxe[0]-mine[0]));
            edge.setAttribute("ui.color", color);
        });

        // To not overload the ThreadProxyPipe
        sleep(500);
    }

    protected void sleep( long ms ) {
        try {
            Thread.sleep( ms );
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}

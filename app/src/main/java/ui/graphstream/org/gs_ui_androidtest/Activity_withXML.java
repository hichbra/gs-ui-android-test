package ui.graphstream.org.gs_ui_androidtest;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;

public class Activity_withXML extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_withxml);
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

        Log.e("Debug",graph+"");

        display(savedInstanceState, graph, true);
    }

    public void display(Bundle savedInstanceState, Graph graph, boolean autoLayout) {
        //Cependant, si le fragment est restauré à partir d'un état précédent, alors il n'y a pas besoin d'effectuer quoi que ce soit et il faudrait juste
        // effectuer le return, sinon nous pourrions nous retrouver avec des fragments qui se chevauchent.
        if (savedInstanceState == null) {
            // Récupérez le FragmentManager
            FragmentManager fm = getFragmentManager();
            // Trouver si le fragment que nous souhaitons afficher appartient à la backstack
            DefaultFragment fragment = (DefaultFragment) fm.findFragmentByTag("fragment_tag");

            if (null == fragment) {
                // Créez le fragment
                fragment = new DefaultFragment();
                fragment.init(graph, autoLayout);
            }
            //Dans notre cas, l'activité a été démarrée avec des paramètres spécifiques contenus dans l'Intent. Passez ces paramètres à votre fragment
            //fragment.setArguments(getIntent().getExtras());
            // Ajoutez le fragment à son layout et effectuez le commit
            FragmentTransaction ft = fm.beginTransaction() ;
            ft.add(R.id.layoutFragment, fragment).commit();
        }
    }
}

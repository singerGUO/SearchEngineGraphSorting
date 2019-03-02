package search.analyzers;

//import datastructures.concrete.dictionaries.ChainedHashDictionary;

import datastructures.concrete.ChainedHashSet;
//import datastructures.concrete.KVPair;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
//import misc.exceptions.NotYetImplementedException;
//import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

//import javax.naming.directory.InitialDirContext;
import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        IDictionary<URI, ISet<URI>> graph = new ChainedHashDictionary<>();
        ISet<URI> allwebpageUri = new ChainedHashSet<>();
        for (Webpage columnPage : webpages) {
            allwebpageUri.add(columnPage.getUri());
        }
        for (Webpage each : webpages) {
            IList<URI> links = each.getLinks();
            ISet<URI> sublist = new ChainedHashSet<>();
            for (URI uri : links) {
                if (uri != each.getUri() && allwebpageUri.contains(uri)) {
                    sublist.add(uri);
                }
            }
            graph.put(each.getUri(), sublist);

        }

        return graph;


    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less than or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {


        // // Step 1: The initialize step should go here

        double d = 0.85;
        double size = graph.size();
        double n = 1 / size;

        IDictionary<URI, Double> old = new ChainedHashDictionary<>();
        IDictionary<URI, Double> newScore = new ChainedHashDictionary<>();
        // IDictionary<URI, Double> update = new ChainedHashDictionary<>();

        //for getting the name easily we should have a set of the uri
        ISet<URI> uri = new ChainedHashSet<>();
        for (KVPair<URI, ISet<URI>> pair : graph) {

            old.put(pair.getKey(), n);
            uri.add(pair.getKey());
        }


        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here

            for (URI start : uri) {
                //each iteration put 0.0 in as first pagerank
                newScore.put(start, 0.0);

                if (graph.get(start).size() == 0) {
                    for (KVPair<URI, Double> pair : old) {
                        double value = pair.getValue();
                        URI key = pair.getKey();
                        newScore.put(key, value + (old.get(pair.getKey()) * d) / uri.size() + (1 - d) / size);

                    }

                }
                for (URI subPair : graph.get(start)) {
                    int subSize = graph.get(start).size();
                    newScore.put(subPair, old.get(subPair) + d * old.get(subPair) / subSize + (1 - d) / size);
                }


            }

            for (KVPair<URI, ISet<URI>> pair : graph) {
                if (old.get(pair.getKey()) - newScore.get(pair.getKey()) >= epsilon) {
                    return old;
                }
            }
            old = newScore;
        }


        return newScore;
        // double size = graph.size();
        // double initial = 1/size;
        // double newSurf = (1-decay)/size;
        // boolean pass = true;
        // IDictionary<URI, Double> older = new ChainedHashDictionary<URI, Double>();
        // ISet<URI> web = new ChainedHashSet<URI>();
        // IDictionary<URI, Double> result = new ChainedHashDictionary<URI, Double>();
        // for (KVPair<URI, ISet<URI>> pair: graph) {
        //     URI uri = pair.getKey();
        //     older.put(uri, initial);
        //     web.add(uri);
        // }
        // for (int i = 0; i < limit; i++) {
        //     for (URI first: web) {
        //         result.put(first, 0.0);
        //     }
        //     for (KVPair<URI, ISet<URI>> pair: graph) {
        //         URI uri = pair.getKey();
        //         double old = older.get(uri);
        //         ISet<URI> links = pair.getValue();
        //         double sizeLocal = links.size();
        //         if (sizeLocal == 0) {
        //             for (URI link: web) {
        //                 double update = result.get(link);
        //                 update += decay*old/size;
        //                 result.put(link, update);
        //             }
        //
        //         } else {
        //             for (URI link: links) {
        //                 double update = result.get(link);
        //                 update += decay*old/sizeLocal;
        //                 result.put(link, update);
        //             }
        //         }
        //         double last = result.get(uri);
        //         last+=newSurf;
        //         result.put(uri, last);
        //     }
        //
        //     for (URI uri: web) {
        //         if (Math.abs(result.get(uri)-older.get(uri)) > epsilon) {
        //             pass = false;
        //         }
        //     }
        //     if (pass) {
        //         return result;
        //     }  else {
        //         for (KVPair<URI, Double> pair: result) {
        //             URI uri = pair.getKey();
        //             double value = pair.getValue();
        //             older.put(uri, value);
        //         }
        //     }
        //
        // }
        // return result;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return pageRanks.get(pageUri);
    }
}

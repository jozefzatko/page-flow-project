function showPageFlowGraph() {

    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", "http://localhost:8089/api/getPageFlowGraph/", false);
    xhttp.setRequestHeader("Content-type", "application/json");
    xhttp.send();
    var graph = JSON.parse(xhttp.responseText);

    for (i=0; i<graph.nodes.length; i++) {
        graph.nodes[i].shape = "box";
    }

    var container = document.getElementById('graph');

    var options = {
        physics:{
            enabled: false
        },
        edges:{
            arrows: {
                to: {
                    enabled: true,
                    scaleFactor:1,
                    type:'arrow'
                },
                middle: {
                    enabled: false,
                    scaleFactor:1,
                    type:'arrow'
                },
                from: {
                    enabled: false,
                    scaleFactor:1,
                    type:'arrow'
                }
            },
            smooth: {
                enabled: true,
                type: "continuous",
                roundness: 0.5
            }
        },
        layout:{
            hierarchical: {
                enabled: false
            },
            improvedLayout: true
        }
    };

    // initialize your network!
    var network = new vis.Network(container, graph, options);
}
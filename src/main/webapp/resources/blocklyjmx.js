class JMXInterface {
    constructor() {
        this.parser = new DOMParser;
        this.xmlser = new XMLSerializer
    }

    stringToBlocklyXML(contents, testPlanDom) {
        let json = JSON.parse(contents);
        this.pmToBlockly(json, testPlanDom); 
        console.log("after jmxToBlockly: ", testPlanDom);
        return testPlanDom
    }

    pmToBlockly(json, doc){
        const rootElement = doc.documentElement;

        //collection
        let block = doc.createElement("block");
        block.setAttribute("type", "b_collection");
        block.setAttribute("deletable", "false");
        block.setAttribute("movable", "false");
        block.setAttribute("id", "1");
        block.setAttribute("disabled", "false");

        let f_name = doc.createElement("field");
        f_name.setAttribute("name", "NAME");
        f_name.textContent = json.info.name;
        let f_props = doc.createElement("field");
        f_props.setAttribute("name", "PROPS");
        f_props.textContent = '<b_collection guiclass="b_collectionGui" testclass="b_collection" testname="Collection" enabled="true"><stringProp name="b_collection.Postman_Id">' + json.info._postman_id + '</stringProp></b_collection>';
        let stm = doc.createElement("statement");
        stm.setAttribute("name", "Folder");
        
        let folders = json.item;
        if(folders.length > 0){
            stm.appendChild(this.createNewBlockR(doc, folders, 0));
            stm.appendChild(doc.createElement("next"));
            block.appendChild(stm);
        }

        block.appendChild(f_name);
        block.appendChild(f_props);
        rootElement.appendChild(block);
    }

    createNewBlockR(doc, folders, num) {
        let name = folders[num].name;
        let block = doc.createElement("block");
        block.setAttribute("type", "b_folder");
        block.setAttribute("id", name);
        let field = doc.createElement("field");
        field.setAttribute("name", "NAME");
        field.textContent = name;
        block.appendChild(field);
        let data = doc.createElement("field");
        data.setAttribute("name", "PROPS");
        data.textContent = '<b_folder guiclass="b_folderGui" testclass="b_folder" testname="Folder" enabled="true"></b_folder>';
        block.appendChild(data);
        if(folders[num].item.length > 0){
            let stm = doc.createElement("statement");
            stm.setAttribute("name", "FLOW");
            stm.appendChild(this.createNewRequestBlock(doc, folders[num].item, 0));
            stm.appendChild(doc.createElement("next"));
            block.appendChild(stm);
        }
        if(num != folders.length - 1){
            let next = doc.createElement("next");
            next.appendChild(this.createNewBlockR(doc, folders, num + 1));
            block.appendChild(next);
        }
        return block
    }

    createNewRequestBlock(doc, items, num){
        let item = items[num];
        let name = item.name;
        let block = doc.createElement("block");
        block.setAttribute("type", "b_request");
        block.setAttribute("id", name);
        let field = doc.createElement("field");
        field.setAttribute("name", "NAME");
        field.textContent = name;
        block.appendChild(field);
        let data = doc.createElement("field");
        data.setAttribute("name", "PROPS");
        data.textContent = `
            <b_request guiclass="b_requestGui" testclass="b_request" testname="Request" enabled="true">
                <stringProp name="Method">${item.request.method}</stringProp>
                <stringProp name="URL">${item.request.url.raw}</stringProp>
                <Header name="Header">
                    <name name="name">name</name>
                    <value name="value">value</value>
                </Header>
            </b_request>
        `;
        block.appendChild(data);
        if(num != items.length - 1){
            let next = doc.createElement("next");
            next.appendChild(this.createNewRequestBlock(doc, items, num + 1));
            block.appendChild(next);
        }
        return block
    }
}


class BlocklyWorkspace {
    constructor() {
        Blockly.defineBlocksWithJsonArray(blocks);
        const options = {
            toolbox: document.getElementById("toolbox"),
            comments: false,
            disable: false,
            maxBlocks: Infinity,
            trashcan: false,
            collapse: true,
            css: true,
            rtl: false,
            sounds: true,
            oneBasedIndex: true,
            move: {
                drag: true,
                scrollbars: true,
                wheel: false
            },
            zoom: {
                controls: true,
                wheel: true,
                startScale: 1,
                maxScale: 1.3,
                minScale: .7,
                scaleSpeed: .1
            }
        };
        const blocklyDiv = document.getElementById("blocklyDiv");
        const self = this;
        this.workspace = Blockly.inject(blocklyDiv, options);
        this.workspace.addChangeListener(function(evt) {
            if ((evt.type === Blockly.Events.MOVE || evt.type === Blockly.Events.CREATE) && evt.workspaceId) {
                var workspace = Blockly.Workspace.getById(evt.workspaceId);
                let blk = workspace.getBlockById(evt.blockId);
                if (blk) {
                    if (!blk.getField("PROPS").getValue()) {
                        blk.setEnabled(true);
                    } else {}
                }
            }
        });
        window.addEventListener("resize", function() {
            self.onresize()
        }, false);
        this.onresize();
        Blockly.svgResize(this.workspace)
    }
    
    onresize() {
        const blocklyWorkspace = document.getElementById("blocklyWorkspace");
        let element = blocklyWorkspace;
        let x = 0;
        let y = 0;
        do {
            x += element.offsetLeft;
            y += element.offsetTop;
            element = element.offsetParent
        } while (element);
        const blocklyDiv = document.getElementById("blocklyDiv");
        blocklyDiv.style.left = x + "px";
        blocklyDiv.style.top = y + "px";
        blocklyDiv.style.width = blocklyWorkspace.offsetWidth - x + "px";
        blocklyDiv.style.height = blocklyWorkspace.offsetHeight - y + "px";
        Blockly.svgResize(this.workspace);
        $("#propsPane").css({
            top: y + "px",
            height: blocklyWorkspace.offsetHeight - y
        })
    }
    newDocument(xml) {
        const newXML = xml ? xml : 
        `<xml>
            <block type="b_collection" deletable="false" movable="false" id="1" disabled="true">
                <field name="NAME">Collection</field>
                <field name="PROPS"></field>
                <statement name="Folder">
                    <block type="b_folder">
                        <field name="NAME">Folder</field>
                        <field name="PROPS"></field>
                        <statement name="FLOW">
                            <block type="b_request">
                                <field name="NAME">HTTP Request 1</field>
                                <field name="PROPS"></field>
                                <next>
                                    <block type="b_request">
                                        <field name="NAME">HTTP Request 2</field>
                                        <field name="PROPS"></field>
                                    </block>
                                </next>
                            </block>
                        </statement>
                    </block>
                    <next></next>
                </statement>
            </block>
        </xml>`;
        this.workspace.clear();
        Blockly.Xml.domToWorkspace(Blockly.Xml.textToDom(newXML), this.workspace);
        if (!xml) {
            this.workspace.getTopBlocks()[0].select()
        }
        this.workspace.clearUndo()
    }
}


class TopBar {
    constructor(blockly) {
        this.jmxi = new JMXInterface;
        this.blockly = blockly;
        const self = this;
        this.setHandlerByClass("fa-file-o", function() {
            self.newDocument();
            if (typeof ga !== "undefined") {
                ga.getAll()[0].send("event", {
                    eventCategory: "file",
                    eventAction: "new"
                })
            }
        });
        this.setHandlerByClass("fa-folder-open-o", function() {
            document.getElementById("file-input").click()
        });
        this.setHandlerByClass("fa-play", function() {
            var xml = Blockly.Xml.workspaceToDom(blockly.workspace);
            //var xml_text = Blockly.Xml.domToText(xml);
            
            //body of request
            let filterArr = [];
            var filterName = xml.getElementsByTagName('block')[0].getElementsByTagName('value')[0].getElementsByTagName('block')[0].getElementsByTagName('field')[0].textContent
            filterArr.push(filterName.toUpperCase());
            let filters = { 'filters' : filterArr};
            console.log(filters);
            fetch('http://localhost:8080/json?delay=1', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify({filters: filterArr})
              })
              .then(response => response.json())
              .then(data => {
                console.log(data)
                window.open('file:///C:/Users/Windows/Desktop/report.html', '_blank');
            })
              .catch(error => console.error(error))
        });

        document.getElementById("file-input").addEventListener("change", function(e) {
            self.openFromFile(e)
        }, false);
    }
    setHandlerByClass(clsname, handler) {
        let found = $("." + clsname);
        for (let n = 0; n < found.length; n++) {
            let obj = $(found[n]);
            if (obj.prop("tagName") === "I") {
                obj.parent().click(handler)
            } else {
                obj.click(handler)
            }
        }
    }
    newDocument(xml) {
        this.blockly.newDocument(xml);
    }
    openFromFile(e) {
        let file = e.target.files[0];
        if (!file) {
            return
        }
        const self = this;
        const reader = new FileReader;
        $("#loaderOverlay").show();
        reader.onload = function(e) {
            $("#loaderOverlay").hide();
            self.loadJMX(e.target.result);
            if (typeof ga !== "undefined") {
                ga.getAll()[0].send("event", {
                    eventCategory: "file",
                    eventAction: "open",
                    eventLabel: "file"
                })
            }
        };
        reader.readAsText(file)
    }

    loadJMX(contents) {
        const testPlanDom = document.implementation.createDocument(null, "xml", null);
        try {
            this.jmxi.stringToBlocklyXML(contents, testPlanDom);
        } catch (e) {
            throw e;
        }
        this.blockly.workspace.clear();
        Blockly.Xml.domToWorkspace(testPlanDom.documentElement, this.blockly.workspace);
        console.log("File opened and loaded successfully");
    }
}


class BlocklyJMX {
    constructor() {
        this.blockly = new BlocklyWorkspace();
        this.topbar = new TopBar(this.blockly);
        //let self = this;
    }
    newDocument(xml) {
        this.topbar.newDocument(xml)
    }
}

FieldElementProperties = function(a, b, c) {
    FieldElementProperties.superClass_.constructor.call(this, a, b, c);
    this.setVisible(false)
};

Blockly.utils.object.inherits(FieldElementProperties, Blockly.FieldLabel);
FieldElementProperties.fromJson = function(a) {
    var b = Blockly.utils.replaceMessageReferences(a.text);
    return new FieldElementProperties(b, void 0, a)
};

FieldElementProperties.prototype.EDITABLE = false;
FieldElementProperties.prototype.SERIALIZABLE = true;
Blockly.fieldRegistry.register("field_element_properties", FieldElementProperties);

let blocks = [{
    "type": "b_collection",
    "message0": "%1 %2 %3 %4 %5",
    "args0": [{
        type: "field_image",
        src: "/resources/img/TestPlan.png",
        width: 16,
        height: 16,
        alt: "*",
        flipRtl: false
    }, {
        type: "field_label_serializable",
        name: "NAME",
        text: "Collection"
    }, {
        type: "input_value",
        name: "Filter",
        align: "RIGHT"
    },{
        "type": "input_statement",
        "name": "Folder",
    }, {
        type: "field_element_properties",
        name: "PROPS",
        text: ""
    }],
    "colour": 0,
    "tooltip": "",
    "helpUrl": ""
  }, {
    type: "b_folder",
    message0: "%1 %2 %3 %4",
    args0: [{
        type: "field_image",
        src: "/resources/img/LogicController.png",
        width: 16,
        height: 16,
        alt: "*",
        flipRtl: false
    }, {
        type: "field_label_serializable",
        name: "NAME",
        text: "Folder"
    }, {
        type: "field_element_properties",
        name: "PROPS",
        text: ""
    },{
        type: "input_statement",
        name: "FLOW",
        check: ["FlowElement"]
    }],
    previousStatement: ["ThreadGroupLevel"],
    nextStatement: ["ThreadGroupLevel"],
    colour: 100,
    tooltip: "",
    helpUrl: ""
}, {
    type: "b_request",
    message0: "%1 %2 %3",
    args0: [{
        type: "field_image",
        src: "/resources/img/Sampler.png",
        width: 16,
        height: 16,
        alt: "*",
        flipRtl: false
    }, {
        type: "field_label_serializable",
        name: "NAME",
        text: "Request"
    }, {
        type: "field_element_properties",
        name: "PROPS",
        text: ""
    }],
    previousStatement: ["FlowElement"],
    nextStatement: ["FlowElement"],
    colour: 60,
    tooltip: "",
    helpUrl: ""
},{
    type: "b_filter_all",
    message0: "%1 %2 %3",
    args0: [{
        type: "field_image",
        src: "/resources/img/Listener.png",
        width: 16,
        height: 16,
        alt: "*",
        flipRtl: false
    }, {
        type: "field_label_serializable",
        name: "NAME",
        text: "All"
    }, {
        type: "field_element_properties",
        name: "PROPS",
        text: ""
    }],
    output: null,
    colour: 10,
    tooltip: "",
    helpUrl: ""
}, {
    type: "b_filter_empty",
    message0: "%1 %2 %3 %4",
    args0: [{
        type: "field_image",
        src: "/resources/img/Listener.png",
        width: 16,
        height: 16,
        alt: "*",
        flipRtl: false
    }, {
        type: "field_label_serializable",
        name: "NAME",
        text: "Empty"
    }, {
        type: "field_element_properties",
        name: "PROPS",
        text: ""
    }, {
        type: "input_value",
        name: "NEXT",
        align: "RIGHT"
    }],
    output: null,
    colour: 10,
    tooltip: "",
    helpUrl: ""
}, {
    type: "b_filter_random",
    message0: "%1 %2 %3 %4",
    args0: [{
        type: "field_image",
        src: "/resources/img/Listener.png",
        width: 16,
        height: 16,
        alt: "*",
        flipRtl: false
    }, {
        type: "field_label_serializable",
        name: "NAME",
        text: "Random"
    }, {
        type: "field_element_properties",
        name: "PROPS",
        text: ""
    }, {
        type: "input_value",
        name: "NEXT",
        align: "RIGHT"
    }],
    output: null,
    colour: 10,
    tooltip: "",
    helpUrl: ""
}, {
    type: "b_filter_invalid",
    message0: "%1 %2 %3 %4",
    args0: [{
        type: "field_image",
        src: "/resources/img/Listener.png",
        width: 16,
        height: 16,
        alt: "*",
        flipRtl: false
    }, {
        type: "field_label_serializable",
        name: "NAME",
        text: "Invalid"
    }, {
        type: "field_element_properties",
        name: "PROPS",
        text: ""
    }, {
        type: "input_value",
        name: "NEXT",
        align: "RIGHT"
    }],
    output: null,
    colour: 10,
    tooltip: "",
    helpUrl: ""
}, {
    type: "b_filter_missing",
    message0: "%1 %2 %3 %4",
    args0: [{
        type: "field_image",
        src: "/resources/img/Listener.png",
        width: 16,
        height: 16,
        alt: "*",
        flipRtl: false
    }, {
        type: "field_label_serializable",
        name: "NAME",
        text: "Missing"
    }, {
        type: "field_element_properties",
        name: "PROPS",
        text: ""
    }, {
        type: "input_value",
        name: "NEXT",
        align: "RIGHT"
    }],
    output: null,
    colour: 10,
    tooltip: "",
    helpUrl: ""
}];

let modifiers = {
    "": [{
        testclass: "Argument",
        properties: {
            "Argument.name": "",
            "Argument.value": "",
            "Argument.metadata": "="
        }
    }, {
        testclass: "HTTPFileArgs",
        properties: {
            "HTTPFileArgs.files": {
                type: "collectionProp",
                name: "HTTPFileArgs.files",
                value: {
                    type: "elementProp",
                    value: "HTTPFileArg"
                }
            }
        }
    }, {
        testclass: "HTTPFileArg",
        properties: {
            "File.path": "",
            "File.paramname": "",
            "File.mimetype": "="
        }
    }, {
        testclass: "Header",
        properties: {
            "Header.name": "",
            "Header.value": ""
        }
    }],
    b_collection: [{
        guiclass: "b_collectionGui",
        priority: null,
        properties: {
            "b_collection.Postman_Id": "",
        },
        testclass: "b_collection",
        testname: "Collection"
    }],
    b_folder: [{
        guiclass: "b_folderGui",
        priority: null,
        properties: { 

        },
        testclass: "b_folder",
        testname: "Folder"
    }],
    b_request: [{
        guiclass: "b_requestGui",
        priority: null,
        properties: {
            "method": "",
            "url": "",
            "Headers": {
                type: "collectionProp",
                value: {
                    type: "elementProp",
                    value: "Header"
                }
            },
            "Params": {
                type: "elementProp",
                value: "Header"
            },
            "Body": {
                type: "elementProp",
                value: "Header"
            }
        },
        testclass: "HTTPSamplerProxy",
        testname: "HTTP Request"
    }], 
    b_filter_all: [{
        guiclass: "AllFilterGui",
        priority: null,
        properties: {},
        testclass: "AllFilter",
        testname: "All"
    }],
    b_filter_empty: [{
        guiclass: "EmptyFilterGui",
        priority: null,
        properties: {},
        testclass: "EmptyFilter",
        testname: "Empty"
    }],
    b_filter_random: [{
        guiclass: "RandomFilterGui",
        priority: null,
        properties: {},
        testclass: "RandomFilter",
        testname: "Random"
    }],
    b_filter_invalid: [{
        guiclass: "InvalidFilterGui",
        priority: null,
        properties: {},
        testclass: "InvalidFilter",
        testname: "Invalid"
    }],
    b_filter_missing: [{
        guiclass: "MissingFilterGui",
        priority: null,
        properties: {},
        testclass: "MissingFilter",
        testname: "Missing"
    }]
    
};


exports.blocks = blocks;
exports.modifiers = modifiers;
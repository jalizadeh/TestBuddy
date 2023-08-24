<%@ include file="header.jspf" %>

<div class="topbar">
    <div class="toolbars">
        <div class="toolbar">
            <a class="fa fa-file-o" title="New..."></a>
            <a class="fa fa-folder-open-o" title="Open..."></a>
            <input id="file-input" type="file" name="name" style="display: none;"/>
        </div>
        <div class="toolbar">
            <a class="fa fa-play" title="Generate report"></a>
        </div>
    </div>
</div>

<div id="blocklyWorkspace">
    <div id="blocklyDiv" style="position: absolute"></div>
</div>
</body>

<xml id="toolbox" style="display: none">
    <!-- <category name="Filters >"> -->
        <block type="b_filter_all"></block>
        <block type="b_filter_empty"></block>
        <block type="b_filter_random"></block>
        <block type="b_filter_invalid"></block>
        <block type="b_filter_missing"></block>
    <!-- </category> -->
</xml>

<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/js-cookie@beta/dist/js.cookie.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/blockly@3.20200123.1/blockly.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/blockly@3.20200123.1/msg/en.js"></script>
<script>let exports = {};</script>

<script src='/resources/blocklyjmx.js'></script>

<script>
    $(function () {
        const app = new BlocklyJMX();
        app.newDocument();
        $("#loaderOverlay").hide();
    });
</script>
</html>
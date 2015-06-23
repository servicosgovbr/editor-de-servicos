package org.jsoup.nodes;

import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@FieldDefaults(level = PRIVATE, makeFinal = true)
public class StandardXmlDeclaration extends Node {

    public StandardXmlDeclaration() {
        super("");
    }

    @Override
    public String nodeName() {
        return "#declaration";
    }

    void outerHtmlHead(StringBuilder accum, int depth, Document.OutputSettings out) {
        accum.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    }

    void outerHtmlTail(StringBuilder accum, int depth, Document.OutputSettings out) {}

    @Override
    public String toString() {
        return outerHtml();
    }
}

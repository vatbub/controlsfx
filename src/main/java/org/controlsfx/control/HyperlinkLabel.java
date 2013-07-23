/**
 * Copyright (c) 2013, ControlsFX
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of ControlsFX, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONTROLSFX BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.controlsfx.control;

import impl.org.controlsfx.skin.HyperlinkLabelSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Skin;

import com.sun.javafx.event.EventHandlerManager;

/**
 * A UI control that will convert the given text into a series of text labels
 * and {@link Hyperlink hyperlinks}, based on the use of delimiter characters
 * to specify where hyperlinks should appear. The delimiter characters are 
 * square braces (that is, [ and ]). To create a hyperlink in a string you would
 * therefore do something like 
 * <code>hyperlinkLabel.setText("Click [here] for more information!");</code>,
 * with the word 'here' appearing as a hyperlink that a use may click. This 
 * approach therefore allows for hyperlinks to be easily embedded within a 
 * label.
 * 
 * <p>Once hyperlinks have been declared in a text string, it is necessary to
 * respond to the user interacting with the hyperlink (most commonly via mouse
 * clicks). To do so, you register a single event handler for action events on
 * the HyperlinkLabel instance, and then determine what to do within that 
 * callback. For example:
 * 
 * <pre>
 * {@code
 * hyperlinkLabel.setOnAction(new EventHandler<ActionEvent>() {
 *     public void handle(ActionEvent event) {
 *         Hyperlink link = (Hyperlink)event.getSource();
 *         final String str = link == null ? "" : link.getText();
 *         switch(str) {
 *             case "here": // do 'here' action
 *                          break;
 *             case "exit": // do exit action
 *                          break;
 *         }
 *     }
 * });}</pre>
 * 
 * <p>This simple single-handler approach was chosen over any more complex 
 * per-hyperlink solution because it is anticipated that most use cases will 
 * normally consist of one, or very few hyperlinks, and it was therefore unlikely
 * that the increased API complexity would be warranted.
 * 
 * <h3>Screenshot</h3>
 * <p>To demonstrate what a HyperlinkLabel looks like, refer to the screenshot
 * below, when the text 
 * <code>"Hello [world]! I [wonder] what hyperlink [you] [will] [click]"</code>
 * was passed in to the HyperlinkLabel instance:
 * 
 * <br/><br/>
 * <center><img src="hyperlinkLabel.png"/></center>
 * 
 * @see Hyperlink
 * @see ActionEvent
 */
public class HyperlinkLabel extends Control implements EventTarget {
    
    /***************************************************************************
     * 
     * Private fields
     * 
     **************************************************************************/
    
    private final EventHandlerManager eventHandlerManager =
            new EventHandlerManager(this);
    
    
    
    /***************************************************************************
     * 
     * Constructors
     * 
     **************************************************************************/
    
    /**
     * Creates an empty HyperlinkLabel instance with no {@link #textProperty() text}
     * specified.
     */
    public HyperlinkLabel() {
        this(null);
    }
    
    /**
     * Creates a HyperlinkLabel instance with the given text value used as the 
     * initial text.
     * 
     * @param text The text to display to the user.
     */
    public HyperlinkLabel(String text) {
        setText(text);
    }
    
    
    
    /**************************************************************************
     * 
     * Public API
     * 
     **************************************************************************/
    
    /**
     * {@inheritDoc}
     */
    @Override protected Skin<?> createDefaultSkin() {
        return new HyperlinkLabelSkin(this);
    }
    
    
    // --- text
    /**
     * The text to display to the user, using the delimiter characters [ and ]
     * to indicate where hyperlinks should be displayed.
     */
    public final StringProperty textProperty() { 
        return text; 
    }
    private final StringProperty text = new SimpleStringProperty(this, "text");
    public final String getText() {
        return text.get();
    }
    public final void setText(String value) {
        text.set(value);
    }
    
    
    // --- onAction
    /**
     * The action, which is invoked whenever a hyperlink is fired. This
     * may be due to the user clicking on the hyperlink with the mouse, or by
     * a touch event, or by a key press.
     */
    public final ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        if (onAction == null) {
            onAction = new SimpleObjectProperty<EventHandler<ActionEvent>>(this, "onAction") {
                @Override protected void invalidated() {
                    eventHandlerManager.setEventHandler(ActionEvent.ACTION, get());
                }
            };
        }
        return onAction;
    }
    private ObjectProperty<EventHandler<ActionEvent>> onAction;

    public final void setOnAction(EventHandler<ActionEvent> value) {
        onActionProperty().set( value);
    }

    public final EventHandler<ActionEvent> getOnAction() {
        return onAction == null ? null : onAction.get();
    }

    
}
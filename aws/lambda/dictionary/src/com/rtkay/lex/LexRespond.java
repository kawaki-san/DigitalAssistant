package com.rtkay.lex;

public class LexRespond {
    private final DialogAction dialogAction;

    public LexRespond(DialogAction dialogAction) {
        this.dialogAction = dialogAction;
    }

    public DialogAction getDialogAction() {
        return dialogAction;
    }
}

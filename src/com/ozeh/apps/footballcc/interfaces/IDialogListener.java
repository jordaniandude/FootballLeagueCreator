package com.ozeh.apps.footballcc.interfaces;

import java.util.ArrayList;

import com.ozeh.apps.footballcc.entities.Group;
import com.ozeh.apps.footballcc.entities.Team;

import android.app.DialogFragment;


public interface IDialogListener {
    public void onDialogPositiveClick(DialogFragment dialog, String champName,ArrayList<Team> listTeams);
    public void onDialogPositiveClick(DialogFragment dialog,String newChamp);
    public void onDialogPositiveClick(DialogFragment dialog,ArrayList<Group> listGroups,String champName);
    
    public void onDialogNegativeClick(DialogFragment dialog);
}

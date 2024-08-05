package br.com.pluri.eventor.view;

import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;

import org.primefaces.event.timeline.TimelineSelectEvent;
import org.primefaces.model.timeline.TimelineEvent;
import org.primefaces.model.timeline.TimelineModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import br.com.etechoracio.common.view.BaseMB;
import br.com.pluri.eventor.business.EventoSB;
import br.com.pluri.eventor.model.Evento;

@Getter
@Setter
@Scope("view")
@Controller
public class BasicTimelineView extends BaseMB {
	
	private TimelineModel model;  
    private boolean selectable = true;  
    private boolean zoomable = true;  
    private boolean moveable = true;  
    private boolean stackEvents = true;  
    private String eventStyle = "box";  
    private boolean axisOnTop;  
    private boolean showCurrentTime = true;  
    private boolean showNavigation = false;
    
    private List<Evento> gerEvent;
    
    @Autowired
    private EventoSB eventoSB;
   
    @PostConstruct 
    protected void initialize() { 
    	gerEventIndex();
        model = new TimelineModel();
        for(Evento event : gerEvent){
        	model.add(new TimelineEvent(event.getTitulo(), event.getDataInicio()));
        }
    }  
    
    public void gerEventIndex(){
    	this.gerEvent = eventoSB.findAll();
    }
   
    public void onSelect(TimelineSelectEvent e) {  
        TimelineEvent timelineEvent = e.getTimelineEvent(); 
    }    
}

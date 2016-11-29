/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlab.analysis.eg1dvcs.eg1dvcs.kenjo;

import org.jlab.clas.pdg.PDGDatabase;
import org.jlab.clas.physics.GenericKinematicFitter;
import org.jlab.clas.physics.Particle;
import org.jlab.clas.physics.PhysicsEvent;
import org.jlab.io.base.DataEvent;
import org.jlab.io.evio.EvioDataBank;
/**
 *
 * @author kenjo
 */
public class EventSelector extends GenericKinematicFitter {
    protected final Double mybeam;
    public EventSelector(double beam) {
        super(beam);
        mybeam = beam;
    }
    
        /**
     * Returns PhysicsEvent object with reconstructed particles.
     * @param event - DataEvent object
     * @return PhysicsEvent : event containing particles.
     */
    @Override
    public PhysicsEvent  getPhysicsEvent(DataEvent  event){
        //if(event instanceof EvioDataEvent){
            //System.out.println("   CHECK FOR  PARTICLE = " + event.hasBank("EVENT::particle"));
        if(event.hasBank("EVENT::particle")){
            EvioDataBank evntBank = (EvioDataBank) event.getBank("EVENT::particle");
            int nrows = evntBank.rows();
            PhysicsEvent  physEvent = new PhysicsEvent();
                physEvent.setBeam(this.mybeam);
                for(int loop = 0; loop < nrows; loop++){

                    int pid    = evntBank.getInt("pid", loop);
                    int status = evntBank.getByte("status", loop);
                    int dcstat = evntBank.getByte("dcstat", loop);
                    int scstat = evntBank.getByte("scstat", loop);
                    if(PDGDatabase.isValidPid(pid)==true){
                        Particle part = new Particle(
                                evntBank.getInt("pid", loop),
                                evntBank.getFloat("px", loop),
                                evntBank.getFloat("py", loop),
                                evntBank.getFloat("pz", loop),
                                evntBank.getFloat("vx", loop),
                                evntBank.getFloat("vy", loop),
                                evntBank.getFloat("vz", loop));
                        if(status>0){
                            physEvent.addParticle(part);
                        }
                    } else {
                        Particle part = new Particle();
                        part.setParticleWithMass(evntBank.getFloat("mass", loop),
                                evntBank.getByte("charge", loop),
                                evntBank.getFloat("px", loop),
                                evntBank.getFloat("py", loop),
                                evntBank.getFloat("pz", loop),
                                evntBank.getFloat("vx", loop),
                                evntBank.getFloat("vy", loop),
                                evntBank.getFloat("vz", loop)
                        );
                        
                        if(status>0){
                            physEvent.addParticle(part);
                        }
                    }
                   
                }
                return physEvent;
            }
            
        //}
        return new PhysicsEvent(this.mybeam);
    }
    
}

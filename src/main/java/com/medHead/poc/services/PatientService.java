package com.medHead.poc.services;

import org.springframework.stereotype.Service;
import com.medHead.poc.entities.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Optional;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service pour gérer les patients et leur attribution à des services médicaux.
 * Utilise un stockage en mémoire pour les besoins de démonstration ou de développement.
 */
@Service
public class PatientService {

    private final List<Patient> patients = new ArrayList<>();                       // Stockage en mémoire des patients
    private final AtomicLong idGenerator = new AtomicLong(1);             // Générateur d'ID unique
    private final Map<String, Integer> specialityDictionary = new HashMap<>();      // Dictionnaire des spécialités

    /**
     * Initialise le dictionnaire des spécialités.
     * Les spécialités sont associées à des groupes de services (par exemple, médecine générale).
     * Dans une version finale, ce dictionnaire pourrait être externalisé dans un fichier JSON ou une base de données.
     */
    public PatientService() {
        specialityDictionary.put("", 5);                                            //Groupe de médecine générale
        specialityDictionary.put("Anesthésie", 1);                                  //Anesthésie
        specialityDictionary.put("Soins intensifs", 1);                             //Anesthésie
        specialityDictionary.put("Oncologie clinique", 2);                          //Oncologie clinique
        specialityDictionary.put("Spécialités dentaires supplémentaires", 3);       //Groupe dentaire
        specialityDictionary.put("Radiologie dentaire et maxillo-faciale", 3);      //Groupe dentaire
        specialityDictionary.put("Endodontie", 3);                                  //Groupe dentaire
        specialityDictionary.put("Chirurgie buccale et maxillo-faciale", 3);        //Groupe dentaire
        specialityDictionary.put("Pathologie buccale et maxillo-faciale", 2);       //Groupe dentaire
        specialityDictionary.put("Orthodontie", 3);                                 //Groupe dentaire
        specialityDictionary.put("Dentisterie pédiatrique", 3);                     //Groupe dentaire
        specialityDictionary.put("Parodontie", 3);                                  //Groupe dentaire
        specialityDictionary.put("Prosthodontie", 3);                               //Groupe dentaire
        specialityDictionary.put("Dentisterie restauratrice", 3);                   //Groupe dentaire
        specialityDictionary.put("Dentisterie de soins spéciaux", 3);               //Groupe dentaire
        specialityDictionary.put("Médecine d'urgence", 4);                          //Médecine d'urgence
        specialityDictionary.put("Médecine interne de soins aigus", 5);             //Groupe de médecine générale
        specialityDictionary.put("Allergie", 5);                                    //Groupe de médecine générale
        specialityDictionary.put("Médecine audiovestibulaire", 5);                  //Groupe de médecine générale
        specialityDictionary.put("Cardiologie", 5);                                  //Groupe de médecine générale
        specialityDictionary.put("Génétique clinique", 5);                          //Groupe de médecine générale
        specialityDictionary.put("Neurophysiologie clinique", 5);                   //Groupe de médecine générale
        specialityDictionary.put("Pharmacologie clinique et thérapeutique", 5);     //Groupe de médecine générale
        specialityDictionary.put("Dermatologie", 5);                                //Groupe de médecine générale
        specialityDictionary.put("Endocrinologie et diabète sucré", 5);             //Groupe de médecine générale
        specialityDictionary.put("Gastroentérologie", 5);                           //Groupe de médecine générale
        specialityDictionary.put("Médecine générale (interne)", 5);                 //Groupe de médecine générale
        specialityDictionary.put("Médecine générale", 5);                           //Groupe de médecine générale
        specialityDictionary.put("Médecine générale (GP) 6 mois", 5);               //Groupe de médecine générale
        specialityDictionary.put("médecine génito-urinaire", 5);                    //Groupe de médecine générale
        specialityDictionary.put("Médecine gériatrique", 5);                        //Groupe de médecine générale
        specialityDictionary.put("Maladies infectieuses", 5);                       //Groupe de médecine générale
        specialityDictionary.put("Oncologie médicale", 5);                          //Groupe de médecine générale
        specialityDictionary.put("Ophtalmologie médicale", 5);                      //Groupe de médecine générale
        specialityDictionary.put("Neurologie", 5);                                  //Groupe de médecine générale
        specialityDictionary.put("Médecine du travail", 5);                         //Groupe de médecine générale
        specialityDictionary.put("Autre", 5);                                       //Groupe de médecine générale
        specialityDictionary.put("Médecine palliative", 5);                         //Groupe de médecine générale
        specialityDictionary.put("Médecine de réadaptation", 5);                    //Groupe de médecine générale
        specialityDictionary.put("Médecine rénale", 5);                             //Groupe de médecine générale
        specialityDictionary.put("Médecine respiratoire", 5);                       //Groupe de médecine générale
        specialityDictionary.put("Rhumatologie", 5);                                //Groupe de médecine générale
        specialityDictionary.put("Médecine du sport et de l'exercice", 5);          //Groupe de médecine générale
        specialityDictionary.put("Santé publique sexuelle et procréative", 6);      //Obstétrique et gynécologie
        specialityDictionary.put("Cardiologie pédiatrique", 7);                     //Groupe pédiatrique
        specialityDictionary.put("Pédiatrie", 7);                                   //Groupe pédiatrique
        specialityDictionary.put("Pathologie chimique", 8);                         //Groupe de pathologie
        specialityDictionary.put("Neuropathologie diagnostique", 8);                //Groupe de pathologie
        specialityDictionary.put("Histopathologie médico-légale", 8);               //Groupe de pathologie
        specialityDictionary.put("Pathologie générale", 8);                         //Groupe de pathologie
        specialityDictionary.put("Hématologie", 8);                                 //Groupe de pathologie
        specialityDictionary.put("Histopathologie", 8);                             //Groupe de pathologie
        specialityDictionary.put("Immunologie", 8);                                 //Groupe de pathologie
        specialityDictionary.put("Microbiologie médicale", 8);                      //Groupe de pathologie
        specialityDictionary.put("Pathologie pédiatrique et périnatale", 8);        //Groupe de pathologie
        specialityDictionary.put("Virologie", 8);                                   //Groupe de pathologie
        specialityDictionary.put("Service de santé communautaire dentaire", 9);     //Groupe Pronostics et gestion de la santé/Santé communautaire
        specialityDictionary.put("Service de santé communautaire médicale", 9);     //Groupe Pronostics et gestion de la santé/Santé communautaire
        specialityDictionary.put("Santé publique dentaire", 9);                     //Groupe Pronostics et gestion de la santé/Santé communautaire
        specialityDictionary.put("Pratique de l'art dentaire", 9);                  //Groupe Pronostics et gestion de la santé/Santé communautaire
        specialityDictionary.put("Santé publique", 9);                              //Groupe Pronostics et gestion de la santé/Santé communautaire
        specialityDictionary.put("Psychiatrie infantile et adolescente", 10);       //Groupe de psychiatrie
        specialityDictionary.put("Psychiatrie légale", 10);                         //Groupe de psychiatrie
        specialityDictionary.put("Psychiatrie générale", 10);                       //Groupe de psychiatrie
        specialityDictionary.put("Psychiatrie de la vieillesse", 10);               //Groupe de psychiatrie
        specialityDictionary.put("Psychiatrie des troubles d'apprentissage", 10);   //Groupe de psychiatrie
        specialityDictionary.put("Psycothérapie", 10);                              //Groupe de psychiatrie
        specialityDictionary.put("Radiologie clinique", 11);                        //Groupe de radiologie
        specialityDictionary.put("Médecine nucléaire", 11);                         //Groupe de radiologie
        specialityDictionary.put("Chirurgie cardiothoracique", 12);                 //Groupe chirurgical
        specialityDictionary.put("Chirurgie générale", 12);                         //Groupe chirurgical
        specialityDictionary.put("Neurochirurgie", 12);                             //Groupe chirurgical
        specialityDictionary.put("Ophtalmologie", 12);                              //Groupe chirurgical
        specialityDictionary.put("Otolaryngologie", 12);                            //Groupe chirurgical
        specialityDictionary.put("Chirurgie pédiatrique", 12);                      //Groupe chirurgical
        specialityDictionary.put("Traumatologie et chirurgie orthopédique", 12);    //Groupe chirurgical
        specialityDictionary.put("Urologie", 12);                                   //Groupe chirurgical
        specialityDictionary.put("Chirurgie vasculaire", 12);                       //Groupe chirurgical
        // Ajout d'autres spécialités...
    }

    /**
     * Récupère la liste de tous les patients.
     * @return Une liste contenant tous les patients
     */
    public List<Patient> getAllPatients() {
        return patients;
    }

    /**
     * Récupère un patient par son ID.
     * @param id L'ID du patient à rechercher
     * @return Un Optional contenant le patient s'il est trouvé, ou vide sinon
     */
    public Optional<Patient> getPatientById(Long id) {
        return patients.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    /**
     * Sauvegarde un nouveau patient dans le stockage en mémoire.
     * Attribue un ID unique au patient et détermine son service en fonction de sa spécialité.
     * @param patient Le patient à sauvegarder
     * @return Le patient sauvegardé avec un ID unique et un service assigné
     */
    public Patient savePatient(Patient patient) {
        // Assigner un serviceId basé sur la spécialité
        patient.setServiceId(specialityDictionary.getOrDefault(patient.getSpecialite(), 0));
        patient.setId(idGenerator.getAndIncrement());
        patients.add(patient);
        return patient;
    }

    /**
     * Supprime un patient par son ID.
     * @param id L'ID du patient à supprimer
     * @return true si le patient a été supprimé, false sinon
     */
    public boolean deletePatient(Long id) {
        return patients.removeIf(p -> p.getId().equals(id));
    }

}

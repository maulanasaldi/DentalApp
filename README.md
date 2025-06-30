# 🦷 Expert System for Dental Disease Diagnosis
This system is a desktop-based application designed to assist in diagnosing dental and oral diseases based on symptoms experienced by patients. The application uses the Forward Chaining method to determine possible diseases based on the selected symptoms.

## 📋 Main Features
### New Patient Registration
Input data for new patients who wish to have a dental check-up.

### New Patient Notification
Displays new patient data on the doctor's dashboard when the receptionist successfully registers a new patient.

### Diagnosis Process
An expert system using the forward chaining method to diagnose diseases based on the symptoms input by the doctor.

### Data Management (CRUD)
Manage patient data, diseases, symptoms, and diagnosis history.

### Reporting
Generate reports, including patient reports, disease reports, symptom reports, and diagnosis history.

## ⚙️ Technologies Used
1. JavaFX: Graphical User Interface (GUI).
2. MySQL: Database management system.
3. Lombok: Simplifies model class coding.
4. JasperReports: For generating printable diagnosis reports.

## 🏗️ Project Structure
```text
src
├── main
│   └── java
│       └── com.example.appsaldi
│           ├── connectiondb
│           ├── controller
│           ├── dao
│           ├── model
│           └── utils
├── resources
│   └── com.example.appsaldi
│       ├── css
│       ├── img
│       ├── report
│       └── view
└── README.md
```

## 🚀 How to Run
1. Clone this repository:
**git clone https://github.com/maulanasaldi/DentalApp.git**
Import the project into your IDE (recommended: IntelliJ IDEA).
3. Ensure MySQL is running and the database is properly configured.
4. Run the project.

## 📌 Notes
* Make sure the JavaFX library and database connection are properly set up.
* You can customize ICD data, symptoms, and diseases as needed.

## 💻 Screenshots
### 1. Login Interface
![Login Interface](images/Login.png)

### 2. Dashboard Interface
The data below is fake data
![Dashboard Interface](images/01Beranda.png)

### 3. Patient Registration Interface
![Patient Registration Interface](images/02RegistrasiPasien.png)

### 4. Diagnostic Interface
![Diagnostic Interface](images/03TampilanLayarDiagnosa.png)

### 5. Diagnostic Results Interface
![Diagnostic Results Interface](images/04TampilanLayarHasilDiagnosa.png)

### 6. Data Interface (Doctor Access)
The data below is fake data
![Data Interface (Doctor Access)](images/05TampilanLayarData(Dokter).png)

### 7. Patient Form Interface
![Patient Form Interface](images/06TampilanFormPasien.png)

### 8. Disease Form Interface
![Disease Form Interface](images/07TampilanFormPenyakit.png)

### 9. Symptom Form Interface
![Symptom Form Interface](images/08TampilanFormGejala.png)

### 10. Diagnosis History Form Interface
![Diagnosis History Form Interface](images/09TampilanFormRiwayatDiagnosa.png)

### 11. Patient Report Interface (Doctor Access)
The data below is fake data
![Dashboard Interface](images/10TampilanLayarLaporan(Dokter).png)

### 12. Patient Data Interface (Receptionist Access)
The data below is fake data
![Patient Data Interface (Receptionist Access)](images/11TampilanLayarData(Resepsionis).png)

### 13. Patient Report Interface (Receptionist Access)
The data below is fake data
![Patient Report Interface (Receptionist Access)](images/12TampilanLayarLaporan(Resepsionis).png)

## 🙋‍♂️ Contribution
Contributions are very welcome!
Feel free to fork this repository, create a new branch, and submit a pull request.

## 📞 Contact
If you have any questions, please contact:

* Name: Maulana Saldi
* Email: maulanasaldi22@gmail.com


import { useEffect, useState, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getSalonById, deleteTreatment, saveTreatment } from "../api/salonApi";
import "../index.css";

const getPriceDisplay = (price) => {
  if (!price || price <= 0) return { label: "Check in salon", color: "#888" };
  return { label: `${price} PLN`, color: "#4caf50" };
};

function SalonDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [salon, setSalon] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [newTreatment, setNewTreatment] = useState({ name: "", price: "" });

  const loadSalon = useCallback(async () => {
    setLoading(true);
    try {
      const data = await getSalonById(id);
      setSalon(data);
    } catch (err) {
      console.error("Failed to load salon:", err);
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadSalon();
  }, [loadSalon]);

  const handleAddTreatment = async (e) => {
    e.preventDefault();
    setIsSaving(true);
    try {
      await saveTreatment(salon.id, newTreatment);
      setNewTreatment({ name: "", price: "" });
      await loadSalon();
    } catch (err) {
      alert("Error adding service: " + err.message);
    } finally {
      setIsSaving(false);
    }
  };

  const handleDeleteTreatment = async (treatmentId) => {
    if (!window.confirm("Are you sure you want to remove this service?")) return;
    try {
      await deleteTreatment(salon.id, treatmentId);
      await loadSalon();
    } catch (err) {
      alert("Error deleting service");
    }
  };

  if (loading) return <div className="p-10 text-white">Loading details...</div>;
  if (!salon) return <div className="p-10 text-white">Salon not found</div>;

  return (
    <div className="salon-details-container" style={{ padding: "40px", maxWidth: "800px", margin: "0 auto", color: "white" }}>
      <button onClick={() => navigate('/')} style={styles.backBtn}>Back</button>

      <h1>{salon.name}</h1>
      <div style={{ marginBottom: "20px" }}>
        <h3>📍 {salon.address}</h3>

        <h3>
            ⭐ <strong>Rating:</strong>{" "}
            <span style={{ color: "#ffd700", fontWeight: "bold" }}>
                {salon.rating && salon.rating > 0
                    ? parseFloat(salon.rating).toFixed(2).replace('.', ',')
                    : "N/A"}
            </span>
            {salon.reviewsCount ? ` (${salon.reviewsCount} reviews)` : ""}
        </h3>

        <h3>💰 <strong>Price Range:</strong> {salon.priceRange ?? "N/A"}</h3>
        <h3>📞 <strong>Phone:</strong> {salon.phone ?? "N/A"}</h3>
        <h3><strong>🌐 Website:</strong> {salon.website ? <a href={salon.website} target="_blank" rel="noopener noreferrer" style={{ color: "#aa3bff" }}>{salon.website}</a> : "N/A"}</h3>
      </div>

      <h3 style={{ borderBottom: "1px solid #444", paddingBottom: "10px" }}>Services</h3>

      <form onSubmit={handleAddTreatment} style={{ margin: "20px 0", display: "flex", gap: "10px" }}>
        <input placeholder="Service name" value={newTreatment.name} onChange={(e) => setNewTreatment(prev => ({ ...prev, name: e.target.value }))} style={styles.input} required />
        <input type="number" placeholder="Price (PLN)" value={newTreatment.price} onChange={(e) => setNewTreatment(prev => ({ ...prev, price: e.target.value }))} style={{ ...styles.input, width: "120px" }} required />
        <button type="submit" disabled={isSaving} style={styles.addBtn}>{isSaving ? "Adding..." : "Add"}</button>
      </form>

      <ul style={{ listStyle: 'none', padding: 0 }}>
        {salon.treatments?.map((t) => {
          const { label, color } = getPriceDisplay(t.price);
          return (
            <li key={t.id || t.name} style={styles.listItem}>
              <span style={{ flex: 1 }}>{t.name}</span>

              <div style={styles.priceActionContainer}>
                <span style={{ color, fontWeight: 'bold', marginRight: '20px', minWidth: '110px', textAlign: 'right' }}>
                  {label}
                </span>
                <button onClick={() => handleDeleteTreatment(t.id)} style={styles.deleteBtn}>Remove</button>
              </div>
            </li>
          );
        })}
      </ul>

      <button onClick={() => navigate(`/edit/${salon.id}`)} style={styles.editBtn}>Edit Salon Information</button>
    </div>
  );
}

const styles = {
  backBtn: { marginTop: "30px", float: "right", padding: "10px 20px", backgroundColor: "#aa3bff", border: "none", color: "white", borderRadius: "5px", cursor: "pointer" },
  input: { padding: "10px", borderRadius: "4px", border: "1px solid #444", backgroundColor: "#2a2a35", color: "white" },
  addBtn: { backgroundColor: "#4caf50", border: "none", color: "white", padding: "10px 20px", borderRadius: "4px", cursor: "pointer" },
  deleteBtn: { backgroundColor: '#ef4444', border: 'none', color: 'white', padding: '6px 12px', borderRadius: '4px', cursor: 'pointer' },
  editBtn: { marginTop: "30px", padding: "12px 24px", backgroundColor: "#aa3bff", border: "none", color: "white", borderRadius: "5px", cursor: "pointer", fontWeight: "bold" },
  listItem: { display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '15px 0', borderBottom: '1px solid #333' },
  priceActionContainer: { display: 'flex', alignItems: 'center', justifyContent: 'flex-end' }
};

export default SalonDetails;
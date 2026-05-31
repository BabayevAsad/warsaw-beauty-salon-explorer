import { useEffect, useState, useCallback, useMemo } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getSalonById, updateSalon } from "../api/salonApi";

const FIELD_CONFIG = {
  name: { label: "Salon Name", placeholder: "e.g. Artem_barber" },
  address: { label: "Address", placeholder: "e.g. wileńska 14b/u14, 03-414, Warszawa" },
  phone: { label: "Phone", placeholder: "e.g. +48 123 456 789" },
  website: { label: "Website", placeholder: "e.g. https://salon-website.pl" },
  rating: { label: "Rating (0-5)", placeholder: "e.g. 5.0" },
  reviewsCount: { label: "Reviews Count", placeholder: "e.g. 150" },
  priceRange: { label: "Price Range", placeholder: "e.g. 20 zł - 270 zł" },
};

function EditSalon() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    name: "", address: "", phone: "", website: "", rating: "", reviewsCount: "", priceRange: ""
  });

  const [loading, setLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);

  const loadSalon = useCallback(async () => {
    setLoading(true);
    try {
      const data = await getSalonById(id);
      setForm({
        name: data.name ?? "",
        address: data.address ?? "",
        phone: data.phone ?? "",
        website: data.website ?? "",
        rating: data.rating !== null ? String(data.rating) : "",
        reviewsCount: data.reviewsCount !== null ? String(data.reviewsCount) : "",
        priceRange: data.priceRange ?? ""
      });
    } catch (err) {
      console.error("Failed to fetch salon:", err);
      alert("Could not load salon details.");
    } finally {
      setLoading(false);
    }
  }, [id]);

  useEffect(() => {
    loadSalon();
  }, [loadSalon]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsSaving(true);

    const payload = {
      ...form,
      rating: form.rating ? parseFloat(form.rating) : null,
      reviewsCount: form.reviewsCount ? parseInt(form.reviewsCount, 10) : null
    };

    try {
      await updateSalon(id, payload);
      alert("Salon updated successfully!");
      navigate(`/salon/${id}`);
    } catch (err) {
      alert("Error: " + (err.message || "Something went wrong"));
    } finally {
      setIsSaving(false);
    }
  };

  if (loading) return <div style={{ color: 'white', padding: '20px' }}>Loading...</div>;

  return (
    <div style={{ padding: "40px", maxWidth: "600px", margin: "0 auto", color: "white" }}>
      <button
        onClick={() => navigate(`/salon/${id}`)}
        style={{ marginBottom: "20px", cursor: 'pointer', background: 'none', border: '1px solid #444', color: 'white', padding: '5px 10px' }}
      >
        ← Back to Salon
      </button>
      <h1>Edit Salon Details</h1>

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
        {Object.entries(FIELD_CONFIG).map(([key, config]) => (
          <div key={key} style={{ display: 'flex', flexDirection: 'column' }}>
            <label style={{ color: '#aaa', marginBottom: '8px', fontSize: '0.9rem' }}>
              {config.label}
            </label>
            <input
              name={key}
              type={key === 'rating' || key === 'reviewsCount' ? 'number' : 'text'}
              step={key === 'rating' ? '0.1' : '1'}
              value={form[key]}
              onChange={handleChange}
              placeholder={config.placeholder}
              style={{ padding: '12px', borderRadius: '5px', border: '1px solid #444', backgroundColor: '#2a2a35', color: 'white', fontSize: '1rem' }}
            />
          </div>
        ))}

        <button
          type="submit"
          disabled={isSaving}
          style={{ padding: '15px', backgroundColor: isSaving ? '#666' : '#aa3bff', border: 'none', color: 'white', borderRadius: '5px', cursor: isSaving ? 'not-allowed' : 'pointer', fontWeight: 'bold' }}
        >
          {isSaving ? "Saving..." : "Save Changes"}
        </button>
      </form>
    </div>
  );
}

export default EditSalon;
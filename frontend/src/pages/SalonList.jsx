import { useEffect, useState, useCallback } from "react";
import { getAllSalons, searchSalons, triggerScraper } from "../api/salonApi";
import SalonCard from "../components/SalonCard";
import debounce from "lodash/debounce";
import "../App.css";

const SkeletonCard = () => (
  <div className="salon-card" style={{ opacity: 0.5, height: "150px", border: "1px solid #333" }}>
    <div style={{ height: '20px', background: '#333', marginBottom: '10px', borderRadius: '4px' }} />
    <div style={{ height: '15px', background: '#333', width: '60%', borderRadius: '4px' }} />
  </div>
);

function SalonList() {
  const [salons, setSalons] = useState([]);
  const [loading, setLoading] = useState(false);
  const [isFetching, setIsFetching] = useState(true);
  const [statusMessage, setStatusMessage] = useState("");

  const [nameQuery, setNameQuery] = useState("");
  const [addressQuery, setAddressQuery] = useState("");

  const [pagination, setPagination] = useState({ currentPage: 0, totalPages: 0 });

  const loadSalons = useCallback(async (page = 0) => {
    setLoading(true);
    try {
      const data = await getAllSalons(page);
      setSalons(data.content || data);
      setPagination({
        currentPage: data.number ?? page,
        totalPages: data.totalPages ?? 0
      });
    } catch (err) {
      console.error("Error loading salons:", err);
    } finally {
      setLoading(false);
      setIsFetching(false);
    }
  }, []);

  useEffect(() => {
    loadSalons();
  }, [loadSalons]);

  const debouncedSearch = useCallback(
    debounce(async (name, addr) => {
      setLoading(true);
      try {
        const results = await searchSalons(name, addr);
        setSalons(results);
      } catch (err) {
        console.error("Search error:", err);
      } finally {
        setLoading(false);
      }
    }, 500),
    []
  );

  const handleTrigger = async () => {
    setLoading(true);
    setStatusMessage("Scraping initiated...");
    try {
      await triggerScraper();
      setStatusMessage("Scraping complete!");
      await loadSalons(0);
    } catch (err) {
      setStatusMessage("Scraper failed: " + err.message);
    } finally {
      setLoading(false);
      setTimeout(() => setStatusMessage(""), 3000);
    }
  };

  if (isFetching) return <div style={{ color: "white", padding: "20px" }}>Loading application...</div>;

  return (
    <div className="salon-container" style={{ padding: "20px", color: "white" }}>
      <h1>Warsaw Beauty Salons</h1>

      <div style={{ marginBottom: "20px" }}>
        <button onClick={handleTrigger} disabled={loading} style={styles.actionBtn}>
          {loading ? "Processing..." : "Load Salons from Booksy"}
        </button>
        {statusMessage && <p style={{ color: "#4caf50" }}>{statusMessage}</p>}
      </div>

      <div className="search-bar-container" style={{ display: "center", marginBottom: "20px" }}>
        <input placeholder="Name..." value={nameQuery} onChange={(e) => setNameQuery(e.target.value)} />
        <input placeholder="Address..." value={addressQuery} onChange={(e) => setAddressQuery(e.target.value)} />
        <button className="search-button" onClick={() => debouncedSearch(nameQuery, addressQuery)}>Search</button>
      </div>

      {loading ? (
        <div className="salon-grid">{[...Array(6)].map((_, i) => <SkeletonCard key={i} />)}</div>
      ) : (
        <div className="salon-grid">
          {salons.length > 0 ? (
            salons.map((s) => <SalonCard key={s.id} salon={s} />)
          ) : (
            <p>No salons found.</p>
          )}
        </div>
      )}

      <div style={{ marginTop: "30px", textAlign: "center" }}>
        <button disabled={pagination.currentPage === 0} onClick={() => loadSalons(pagination.currentPage - 1)}>Previous</button>
        <span style={{ margin: "0 20px" }}>Page {pagination.currentPage + 1} of {pagination.totalPages}</span>
        <button disabled={pagination.currentPage >= pagination.totalPages - 1} onClick={() => loadSalons(pagination.currentPage + 1)}>Next</button>
      </div>
    </div>
  );
}

const styles = {
  actionBtn: { padding: "10px 20px", backgroundColor: "#aa3bff", color: "white", border: "none", borderRadius: "5px", cursor: "pointer" }
};

export default SalonList;
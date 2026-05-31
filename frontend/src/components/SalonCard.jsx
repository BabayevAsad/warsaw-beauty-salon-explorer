import { useNavigate } from "react-router-dom";
import styles from "../SalonCard.module.css";

function SalonCard({ salon }) {
  const navigate = useNavigate();

  return (
    <div
      onClick={() => navigate(`/salon/${salon.id}`)}
      className={styles.card}
    >
      <h3 className={styles.title}>{salon.name}</h3>
      <p className={styles.address}>📍 {salon.address}</p>

      <div className={styles.footer}>
        <span style={{ color: "#ffd700", fontWeight: "bold" }}>
          ⭐ {salon.rating && salon.rating > 0
               ? parseFloat(salon.rating).toFixed(2).replace('.', ',')
               : "N/A"}
        </span>
        <span style={{ color: "#4caf50" }}>
          {salon.priceRange || "Price not specified"}
        </span>
      </div>
    </div>
  );
}

export default SalonCard;
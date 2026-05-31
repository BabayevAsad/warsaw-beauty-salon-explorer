const BASE_URL = "http://localhost:8080/rest/api/salons";

export const getAllSalons = async (page = 0) => {
  const response = await fetch(`${BASE_URL}?page=${page}&size=12`);
  return handleResponseError(response);
};

export const getSalonById = async (id) => {
  const response = await fetch(`${BASE_URL}/${id}`);
  return handleResponseError(response);
};

export const updateSalon = async (id, data) => {
  const response = await fetch(`${BASE_URL}/update/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(data),
  });
  return handleResponseError(response);
  };

export const searchSalons = async (name, address) => {
  const params = new URLSearchParams();
  if (name) params.append("name", name);
  if (address) params.append("address", address);

  const response = await fetch(`${BASE_URL}/search?${params.toString()}`);
  return handleResponseError(response);
};

export const deleteTreatment = async (salonId, treatmentId) => {
  const response = await fetch(`http://localhost:8080/rest/api/treatments/${treatmentId}`, {
    method: "DELETE",
    headers: { "Content-Type": "application/json" },
  });

  return handleResponseError(response);
};

export const saveTreatment = async (salonId, treatmentData) => {
  const response = await fetch(`http://localhost:8080/rest/api/treatments/${salonId}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(treatmentData),
  });

  return handleResponseError(response);
};

export const triggerScraper = async () => {
  const response = await fetch("http://localhost:8080/api/scraper/trigger", {
    method: "POST",
  });
  return handleResponseError(response);
};

const handleResponseError = async (response) => {
  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}));
    throw new Error(errorData.body || errorData.message || "An unexpected error occurred");
  }

  if (response.status === 204) return true;

  return response.json();
};
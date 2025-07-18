import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    stages: [
        { duration: '30s', target: 10 }, // montée à 10 utilisateurs
        { duration: '1m', target: 10 },  // maintien
        { duration: '30s', target: 0 },  // descente
    ],
    thresholds: {
        http_req_duration: ['p(95)<500'],
        'http_req_failed': ['rate<0.01'],
    },
};

const BASE_URL = 'http://hotelservice.niceocean-6b010897.westus2.azurecontainerapps.io';

export default function () {
    // 1. GET all reservations
    let res = http.get(`${BASE_URL}/reservations/getAll`);
    check(res, {
        'getAll status 200': (r) => r.status === 200,
    });

    let payload = JSON.stringify({
        id: null,
        guestName: `Guest${__VU}_${Date.now()}`,
        roomNumber: Math.floor(Math.random() * 100) + 1,
    });
    let params = { headers: { 'Content-Type': 'application/json' } };
    res = http.post(`${BASE_URL}/reservations/addReservation`, payload, params);
    check(res, {
        'create reservation status 200': (r) => r.status === 200,
        'response has id': (r) => r.json('id') !== null,
    });

    const reservationId = res.json('id');
    res = http.get(`${BASE_URL}/reservations/getReservation/${reservationId}`);
    check(res, {
        'getReservation status 200': (r) => r.status === 200,
    });

    let updatePayload = JSON.stringify({
        id: reservationId,
        guestName: `UpdatedGuest${__VU}`,
        roomNumber: Math.floor(Math.random() * 100) + 1,
    });
    res = http.put(`${BASE_URL}/reservations/update/${reservationId}`, updatePayload, params);
    check(res, {
        'update reservation status 200': (r) => r.status === 200,
        'guestName updated': (r) => r.json('guestName').startsWith('UpdatedGuest'),
    });

    res = http.del(`${BASE_URL}/reservations/delete/${reservationId}`);
    check(res, {
        'delete reservation status 204': (r) => r.status === 204,
    });

    sleep(1);
}

import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 10,
  duration: '30s',
};

export default function () {
  let res = http.get('http://hotel-service/api/products');
  check(res, { 'status was 200': (r) => r.status === 200 });
  let payload = JSON.stringify({ name: 'k6', price: 1.23, quantity: 1 });
  let headers = { 'Content-Type': 'application/json' };
  let postRes = http.post('http://hotel-service/api/products', payload, { headers });
  check(postRes, { 'post status was 200': (r) => r.status === 200 });
  sleep(1);
} 
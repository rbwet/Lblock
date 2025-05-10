# Lblock

Lblock is a blockchain-based project designed to implement core blockchain functionalities, including mining, peer-to-peer networking, and consensus mechanisms. This repository contains the full implementation of the blockchain, along with a set of tools for managing and interacting with the chain.

## Features

* Peer-to-peer networking with decentralized consensus
* Proof of Work (PoW) mining
* Transaction validation and block creation
* Secure and immutable chain of blocks
* Modular design for easy extension

## Installation

1. **Clone the repository**:

   ```bash
   git clone https://github.com/rbwet/Lblock.git
   cd Lblock
   ```

2. **Install dependencies**:

   ```bash
   pip install -r requirements.txt
   ```

3. **Build the project**:

   ```bash
   python setup.py install
   ```

## Usage

To start a node:

```bash
python main.py --node
```

To mine a new block:

```bash
python main.py --mine
```

To view the blockchain:

```bash
python main.py --view
```

## Configuration

Configuration settings can be adjusted in the `config.json` file:

* **block\_time**: Time interval for block creation
* **difficulty**: Mining difficulty
* **network\_port**: Port for P2P communication

## Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository.
2. Create a new branch (`feature-branch`).
3. Make your changes.
4. Submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

For questions or collaboration inquiries, reach out at [robbiewetz11@gmail.com](mailto:robbiewetz11@gmail.com).
